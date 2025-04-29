
// Fonction pour créer un favori dans Firebase
import {addDoc, collection, deleteDoc, getDocs, getFirestore, query, where} from "firebase/firestore";
import {getCurrentDateTime} from "./UtiliService";

const db=getFirestore();
export const createHistorique = async (idUtilisateur, idCryptomonnaie , operation) => {
    try {
        const objRef = collection(db, "historiqueCrypto");

        const dateChangement = getCurrentDateTime();

        await addDoc(objRef, { dateChangement ,idCryptomonnaie,  idUtilisateur ,operation  });
    } catch (error) {
        console.error("Erreur lors de l'ajout du favori:", error);
    }
};
