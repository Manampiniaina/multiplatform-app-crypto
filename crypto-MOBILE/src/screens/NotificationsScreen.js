import React, { useEffect, useState } from 'react';
import {View, Text, StyleSheet, ScrollView, ActivityIndicator} from 'react-native';
import {
  collection,
  query, 
  where, 
  getDocs
} from 'firebase/firestore';
import Header from '../components/Header';
import NavBar from '../components/NavBar';
import theme from '../styles/theme';
import {db} from "../configuration/FirebaseConfig";
import { useUser } from "../contexts/Context";

function formatNotificationDate(dateObj) {
  const now = new Date();
  const diffMs = now - dateObj;
  const diffSec = Math.floor(diffMs / 1000);
  const diffMin = Math.floor(diffSec / 60);
  const diffHrs = Math.floor(diffMin / 60);

  if (diffSec < 60) {
    return "à l'instant";
  } else if (diffMin < 60) {
    return `il y a ${diffMin} minute${diffMin > 1 ? "s" : ""}`;
  } else if (diffHrs < 24) {
    return `il y a ${diffHrs} heure${diffHrs > 1 ? "s" : ""}`;
  } else {
    // Pour plus de 24 heures, afficher la date complète au format "le 7 février 2024 à 20:45"
    const datePart = dateObj.toLocaleDateString('fr-FR', {
      day: 'numeric',
      month: 'long',
      year: 'numeric'
    });
    const timePart = dateObj.toLocaleTimeString('fr-FR', {
      hour: '2-digit',
      minute: '2-digit'
    });
    return `le ${datePart} à ${timePart}`;
  }
}

async function getFavoriteCryptoNotifications(currentUserId) {
  const favoritesRef = collection(db, "cryptoFavori");
  const favoritesQuery = query(favoritesRef, where("iduser", "==", currentUserId));
  const querySnapshot = await getDocs(favoritesQuery);
  
  const favoriteIdCryptos = [];
  querySnapshot.forEach((doc) => {
    const data = doc.data();
    if (data.idcrypto) {
      console.log("Crypto favorite trouvée :", data.idcrypto);
      favoriteIdCryptos.push(data.idcrypto);
    }
  });
  
  return favoriteIdCryptos;
}
async function getRemoteMouvementsForUser(userId) {
  try {
    const mouvementsRef = collection(db, "fondAttente");
    
    const q = query(
      mouvementsRef,
      where("idUtilisateur", "==", userId),
      where("isRemoting", "==", false)
    );
    
    const querySnapshot = await getDocs(q);
    const results = [];
    
    querySnapshot.forEach((doc) => {
      results.push(doc.data());
    });
    
    return results;
  } catch (error) {
    console.error("Erreur lors de la récupération des mouvements distants :", error);
    return [];
  }
}
async function getCryptoNameById(cryptoId) {
  const cryptosRef = collection(db, "cryptomonnaies");
  const cryptoQuery = query(cryptosRef, where("idCrypto", "==", cryptoId));
  const querySnapshot = await getDocs(cryptoQuery);
  let cryptoName = cryptoId;
  querySnapshot.forEach((doc) => {
    const data = doc.data();
    if (data.nom) {
      cryptoName = data.nom;
    }
  });
  return cryptoName;
}
async function getUserNameById(userId) {
  try {
    const usersRef = collection(db, "utilisateurs");

    const q = query(usersRef, where("id", "==", userId));
    const querySnapshot = await getDocs(q);
    if (!querySnapshot.empty) {
        console.log("utilisateur:"+JSON.stringify(querySnapshot.docs[0].data().nom) );
        return querySnapshot.docs[0].data().nom +"  "+ querySnapshot.docs[0].data().prenom;
    } else {
        throw new Error("Aucun utilisateur trouvé avec cet id.");
    }
} catch (error) {
    console.error("Erreur lors de la récupération du nom d'utilisateur:", error);
    return userId;
  }
}

async function getNotificationsFromFavorites(currentUserId) {
  // Récupère les identifiants favoris
  const notifications = [];
  const mouvements = await getRemoteMouvementsForUser(currentUserId);
console.log("Mouvements distants pour l'utilisateur", currentUserId, ":", mouvements);
  for (const mvt of mouvements) {
    let dateObj;
    //  dateObj = mvt.dateMouvement.toDate();
         // const formattedDate = formatNotificationDate(dateObj);
    const typeMessage = mvt.signe === 1 ? "dépôt" : "retrait";
    const message = `Votre ${typeMessage} d'un montant de ${mvt.montant} est validé ${mvt.dateMouvement}.`;
    console.log("mess",message);
    notifications.push( message);
  }
  console.log("Notif générées :", notifications);
  const favoriteIds = await getFavoriteCryptoNotifications(currentUserId);
  console.log("Liste des favoris :", favoriteIds);
  


  // Si aucun favori n'est trouvé, retourner un tableau vide
  if (favoriteIds.length === 0) {
    console.log("Aucun favori trouvé pour l'utilisateur", currentUserId);
    return notifications;
  }
  
  // Pour chaque favori, interroge la collection "transaction_crypto"
  for (const favId of favoriteIds) {
    const transactionsRef = collection(db, "transactionCrypto"); // Assurez-vous que le nom correspond exactement
    const transactionsQuery = query(
      transactionsRef,
      where("idcrypto", "==", favId)
    );
    console.log("Interrogation des transactions pour :", favId);
    const transactionSnapshot = await getDocs(transactionsQuery);
    
  for (const docSnap of transactionSnapshot.docs) {
      const data = docSnap.data();
      if (data.quantite && data.dateheure) {
        const cryptoName = await getCryptoNameById(favId);
        const action = data.idacheteur != null ? "a acheté" : "a vendu";
        const iduser = data.idacheteur != null ? data.idacheteur : data.idvendeur;
        const usename = await getUserNameById(iduser);
        const dateObj = data.dateheure.toDate();
        const formattedDate = formatNotificationDate(dateObj);
        const message = `${usename} ${action} votre crypto favorite ${cryptoName} lee ${formattedDate}.`;
        notifications.push({ message, date: dateObj });
            } else {
        console.log("Transaction sans données suffisantes :", data);
      }
    }
  }
  
  notifications.sort((a, b) => b.date - a.date);
  
  // On retourne uniquement les messages
  const messages = notifications.map(n => n.message);
  console.log("Notifications générées :", messages);
  return messages;
}
const NotificationsScreen = () => {
  const [loading, setLoading] = useState(false);
  // Identifiant de l'utilisateur courant (à remplacer par la valeur dynamique dans votre appli)
 
const { utilisateur, setUser } = useUser(); // Assurez-vous que le contexte fournit bien setUser
const user = utilisateur;
 const currentUserId = user.id;
  console.log(user.id);
  const [notifMessages, setNotifMessages] = useState([]);
    useEffect(() => {
    const fetchNotifications = async () => {
      setLoading(true);
      const notifs = await getNotificationsFromFavorites(currentUserId);
      setNotifMessages(notifs);
      setLoading(false);

    };

    fetchNotifications();
  }, [currentUserId]);
  
  return (
    <View style={styles.container}>
      <Header title="Notifications" />
      {loading && <ActivityIndicator size="large" color={theme.colors.primary} style={styles.loadingIndicator} />}

      <ScrollView style={styles.notificationsContainer}>
        {notifMessages.length > 0 ? (
          notifMessages.map((msg, index) => (
            <View key={index} style={styles.notificationItem}>
              <Text style={styles.notificationText}>{msg}</Text>
            </View>
          ))
        ) : (
          <Text style={styles.notificationText}>Aucune notification pour le moment.</Text>
        )}
      </ScrollView>
      <NavBar activeScreen="Notifications" />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: theme.colors.background,
    padding: 20,
  },
  title: {
    fontSize: 28,
    fontWeight: 'bold',
    color: theme.colors.primary,
    marginBottom: 20,
    textAlign: 'center',
  },
  notificationsContainer: {
    flex: 1,
  },
  notificationItem: {
    backgroundColor: theme.colors.surface,
    padding: 15,
    marginBottom: 15,
    borderRadius: 10,
    // Ombres pour iOS
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.2,
    shadowRadius: 4,
    // Élévation pour Android
    elevation: 2,
  },
  notificationText: {
    fontSize: 16,
    color: theme.colors.text,
  },
});
export default NotificationsScreen;
