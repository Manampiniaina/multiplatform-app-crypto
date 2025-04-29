import React, { useState,useEffect } from 'react';
import { View, Text, Image, TouchableOpacity, StyleSheet, ScrollView } from 'react-native';
import { Ionicons, FontAwesome } from '@expo/vector-icons';
import * as ImagePicker from 'expo-image-picker';
import NavBar from '../components/NavBar';
import theme from '../styles/theme';
import { useNavigation } from '@react-navigation/native';
import { useUser } from "../contexts/Context";
import { getPortefeuilles } from "../services/TransactionCryptoService";
import { useCryptos } from "../contexts/Context";

// Importations Firebase
import { initializeApp } from 'firebase/app';
import { 
    getFirestore, 
    collection, 
    query, 
    where, 
    getDocs, 
    updateDoc ,
  addDoc
  } from 'firebase/firestore';
  import Header from "../components/Header";
import {db} from "../configuration/FirebaseConfig";



// Configuration Firebase
const secondfirebaseConfig = {
    apiKey: "AIzaSyAkWgOb-qPFZns2h5gBzwqxz0hUQ_fbKas",
  authDomain: "mobile-crypto-a22eb.firebaseapp.com",
  projectId: "mobile-crypto-a22eb",
  storageBucket: "mobile-crypto-a22eb.firebasestorage.app",
  messagingSenderId: "962618509326",
  appId: "1:962618509326:web:3bc7fe14f0c65ec419945f"
};

// Initialisation Firebase
const secondapp = initializeApp(secondfirebaseConfig);
const seconddb = getFirestore(secondapp);

// Fonction pour mettre à jour l'URL de l'image de l'utilisateur dans Firestore
async function updateUserImageUrl(userId, newImageUrl) {
    console.log("debut mise a jour");
    const usersCollection = collection(seconddb, "utilisateurs");
  const q = query(usersCollection, where("id", "==", userId));
  const querySnapshot = await getDocs(q);
  
  if (!querySnapshot.empty) {
    const userDocRef = querySnapshot.docs[0].ref;
    await updateDoc(userDocRef, { lienImage: newImageUrl });
    console.log(`User ${userId} mis à jour : lienImage = ${newImageUrl}`);
  } else {
    console.error(`ERREUR: Aucun document trouvé avec id="${userId}"`);
  }
}
const CLOUDINARY_UPLOAD_PRESET = "cdnCrypto2"; 
const CLOUDINARY_CLOUD_NAME = "ds4y4okg3"; 

const uploadImageToCloudinary = async (imageUri) => {
  console.log("Début de l'upload...");
  const fileType = imageUri.split('.').pop();
  console.log(`Type de fichier : ${fileType}`);
  console.log("imageUri : " + imageUri);

  const formData = new FormData();
  formData.append("file", {
      uri: imageUri,
      type: `image/${fileType}`,
      name: `photo.${fileType}`,
  });
  formData.append("upload_preset", CLOUDINARY_UPLOAD_PRESET);

  console.log("FormData préparé...");

  try {
      console.log("Envoi de la requête...");
      const response = await fetch(
          `https://api.cloudinary.com/v1_1/${CLOUDINARY_CLOUD_NAME}/image/upload`,
          {
              method: "POST",
              body: formData,
          }
      );
      console.log("Réponse reçue...");
      if (!response.ok) {
          throw new Error(`Erreur HTTP ${response.status}`);
      }
      const data = await response.json();
      console.log("Réponse Cloudinary :", data);
      return data.secure_url; // Retourne l'URL de l'image hébergée
  } catch (error) {
      console.error("Erreur lors de l'upload :", error);
      return null;
  }
};


async function insertHistoriqueOperationUtilisateur(idUtilisateur, lienImage) {
    console.log("Début de insertHistoriqueOperationUtilisateur...");
    
    try {
        const historiqueRef = collection(db, "historiqueOperationUtilisateur");

        const data = {
            dateChangement: new Date(new Date().getTime() + 1 * 60 * 1000), // Ajouter 2 minutes
            operation: "UPDATE",
            utilisateur: {
                id: idUtilisateur,
                lienImage: lienImage
            }
        };
        
        

        const docRef = await addDoc(historiqueRef, data);
        console.log("Document inséré avec succès:", docRef.id);

   //     await updateUserImageUrl(idUtilisateur, lienImage);
    } catch (error) {
        console.error("Erreur lors de l'insertion:", error);
    }
}



const ProfileScreen = () => {

  const { utilisateur, setUser } = useUser(); // Assurez-vous que le contexte fournit bien setUser
  const user = utilisateur;
  const { cryptos } = useCryptos();

  const [portefeuilles, setPortefeuilles] = useState([]);
  useEffect(() => {
    const fetchPortefeuilles = async () => {
        const data = await getPortefeuilles(user.id, cryptos);
        setPortefeuilles(data);
    };

    fetchPortefeuilles();
}, [user.id, cryptos]);

  const pickImage = async (fromCamera) => {
      let result;
      if (fromCamera) {
          result = await ImagePicker.launchCameraAsync({
              mediaTypes: ImagePicker.MediaTypeOptions.Images,
              allowsEditing: true,
              aspect: [1, 1],
              quality: 1,
          });
      } else {
          result = await ImagePicker.launchImageLibraryAsync({
              mediaTypes: ImagePicker.MediaTypeOptions.Images,
              allowsEditing: true,
              aspect: [1, 1],
              quality: 1,
          });
      }
      if (!result.canceled) {
          const imageUri = result.assets[0].uri;
          const cloudinaryUrl = await uploadImageToCloudinary(imageUri);
          console.log("cloudinaryUrl:", cloudinaryUrl);
          console.log("user.id:", user.id);
        //  setUser({ ...user, lienImage: cloudinaryUrl });
          updateUserImageUrl(user.id, cloudinaryUrl);
          insertHistoriqueOperationUtilisateur(user.id, cloudinaryUrl);


      }
  };

  return (

    <View style={styles.container}>
        <Header title="Profile"/>
        <ScrollView contentContainerStyle={styles.scrollContent}>

            <Image
                source={user.lienImage ? { uri: user.lienImage } : require('../../assets/user.png')}
                style={styles.profileImage}
            />

            {/* Email sous la photo */}
            <Text style={styles.nameText}>{user.nom} {user.prenom} </Text>
            <Text style={styles.emailText}>{user.mail}</Text>

            <View style={styles.buttonContainer}>
                <TouchableOpacity style={styles.iconButton} onPress={() => pickImage(true)}>
                    <Ionicons name="camera" size={30} color={theme.colors.text} />
                </TouchableOpacity>
                <TouchableOpacity style={styles.iconButton} onPress={() => pickImage(false)}>
                    <FontAwesome name="photo" size={30} color={theme.colors.text} />
                </TouchableOpacity>

            </View>

            {/* Solde actuel après les icônes */}
            <View style={styles.balanceContainer}>
                <Text style={styles.balanceText}>💰 Solde actuel : {user.solde}€</Text>
            </View>

            {/* Liste des cryptos détenues */}
            <Text style={styles.sectionTitle}>Mes Cryptos</Text>

            {/* Affichage du portefeuille */}
            {portefeuilles.length === 0 ? (
                <Text style={styles.noCryptoText}>Aucune crypto détenue</Text>
            ) : (
                portefeuilles.map((crypto) => (
                    <View key={crypto.idCrypto} style={styles.cryptoItem}>
                        <Text style={styles.cryptoName}>{crypto.nom}</Text>
                        <Text style={styles.cryptoQuantity}>{crypto.quantite}</Text>
                    </View>
                ))
            )}

        </ScrollView>

        {/* Barre de navigation */}
        <NavBar activeScreen="Profile" />
    </View>
);
};

const styles = StyleSheet.create({

container: {
    flex: 1,
    backgroundColor: theme.colors.background,
    padding: 20,
},
scrollContent: {
    alignItems: 'center',
    paddingHorizontal: 20,
},
profileImage: {
    width: 120,
    height: 120,
    borderRadius: 60,
    marginBottom: 10,
},
nameText: {
    fontSize: 18,
    color: theme.colors.text,
    marginBottom: 20,
},
emailText: {
    fontSize: 14,
    color: theme.colors.secondary,
    marginBottom: 20,
},
buttonContainer: {
    flexDirection: 'row',
    justifyContent: 'center',
    marginBottom: 20,
},
iconButton: {
    backgroundColor: theme.colors.primary,
    padding: 15,
    borderRadius: 30,
    marginHorizontal: 10,
},
iconConfirm: {
    backgroundColor: 'green',
    padding: 15,
    borderRadius: 30,
    marginHorizontal: 10,
},
balanceContainer: {
    alignItems: 'center',
    marginBottom: 20,
},
balanceText: {
    fontSize: 22,
    color: theme.colors.primary,
    fontWeight: '700',
},
sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: theme.colors.text,
    marginBottom: 10,
},
noCryptoText: {
    fontSize: 16,
    color: theme.colors.secondary,
    textAlign: 'center',
    marginVertical: 10,
},
cryptoItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    width: '100%',
    paddingVertical: 10,
    borderBottomWidth: 1,
    borderBottomColor: theme.colors.primary,
},
cryptoName: {
    fontSize: 16,
    color: theme.colors.text,
},
cryptoQuantity: {
    fontSize: 16,
    fontWeight: 'bold',
    color: theme.colors.text,
},
});

export default ProfileScreen;
