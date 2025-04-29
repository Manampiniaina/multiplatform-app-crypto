import {addDoc, collection, getFirestore} from "firebase/firestore";


const db = getFirestore(); // 🔥 Initialisation de Firestore

export const addOperation = async (
    idUtilisateur ,
    montant ,
    operation
) => {
    try {
        let signe;
        if(operation==="depot"){
            signe=1;
        }else {
            signe=-1;
        }
        const date = new Date();

        const options = {
            timeZone: "Europe/Moscow",
            day: "numeric",
            month: "long",
            year: "numeric",
            hour: "2-digit",
            minute: "2-digit",
            second: "2-digit",
            hour12: false
        };

        const dateMouvement = `${date.toLocaleTimeString("fr-FR", options)} UTC+3`;
        const isRemoting =false;
        const objRef = collection(db, "fondAttente");
        await addDoc(objRef, {dateMouvement ,  idUtilisateur, montant , signe ,isRemoting });
        console.log("Operation demande ajouté avec succès !");
    } catch (error) {
        console.error("Erreur lors de l'ajout de l' operation:", error);
    }
};
