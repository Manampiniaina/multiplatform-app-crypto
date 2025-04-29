import { collection, getDocs, query, where } from "firebase/firestore";
import { db } from "../configuration/FirebaseConfig";

export const getFond = async (utilisateurId) => {
    console.log(`Get fond ${utilisateurId}`);
    try {
        // Récupérer les transactions de l'utilisateur
        const transactionsRef = collection(db, "fonds");
        const q = query(transactionsRef, where("utilisateur", "==", utilisateurId));
        const querySnapshot = await getDocs(q);

        let totalFond = 0;
        querySnapshot.forEach((doc) => {
            const data = doc.data();
            console.log("data zao:"+JSON.stringify( data));
            totalFond += data.montant || 0;
        });

        return totalFond;
    } catch (error) {
        console.error("Erreur lors de la récupération des fonds :", error);
        return 0;
    }
};
