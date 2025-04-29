import { collection, query, where, getDocs, getFirestore } from "firebase/firestore";
import {getFond} from "./FondService";

export const getByMail = async (mail) => {
    try {
        const db = getFirestore(); // 🔥 Assure-toi que Firestore est bien initialisé
        const usersRef = collection(db, "utilisateurs");

        const allUsersSnapshot = await getDocs(usersRef);
        const allUsers = allUsersSnapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));
        console.log("Tous les utilisateurs :", JSON.stringify(allUsers, null, 2));

        console.log("Recherche de l'email:", mail);
        const q = query(usersRef, where("mail", "==", mail));
        const querySnapshot = await getDocs(q);

        if (!querySnapshot.empty) {
            const utilisateur =  querySnapshot.docs[0].data();
            if (utilisateur) {
                utilisateur.solde = await getFond(utilisateur.id);
            }
            console.log("utilisateur:"+JSON.stringify( utilisateur));
            return utilisateur;
        } else {
            throw new Error("Aucun utilisateur trouvé avec cet email.");
        }
    } catch (error) {
        console.error("Erreur dans getByMail:", error);
        return null;
    }
};
