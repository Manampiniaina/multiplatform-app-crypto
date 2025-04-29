import { collection, getDocs, getFirestore, query, where } from "firebase/firestore";

const db = getFirestore();

export const getAllTransactionsVentes = async (id) => {
    try {
        console.log("Fetching Transactions Ventes...");
        const objsRef = collection(db, "portefeuille");
        const q = query(objsRef, where("vendeur", "==", id));
        const snapshot = await getDocs(q);
        const objs = snapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));
        console.log("Transactions de vente de l'utilisateur :", id, JSON.stringify(objs));
        return objs;
    } catch (error) {
        console.error("Erreur lors de la récupération des ventes :", error);
        return [];
    }
};

export const getAllTransactionsAchats = async (id) => {
    try {
        console.log("Fetching Transactions Achats...");
        const objsRef = collection(db, "portefeuille");
        const q = query(objsRef, where("acheteur", "==", id));
        const snapshot = await getDocs(q);
        const objs = snapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));
        console.log("Transactions d'achat de l'utilisateur :", id, JSON.stringify(objs));
        return objs;
    } catch (error) {
        console.error("Erreur lors de la récupération des achats :", error);
        return [];
    }
};
export const getPortefeuilles = async (id, cryptos) => {
    try {
        const achats = await getAllTransactionsAchats(id);
        const ventes = await getAllTransactionsVentes(id);
        
        const portefeuille = cryptos.map(crypto => {
            const totalQuantiteAchat = achats
                .filter(tx => tx.idCrypto === crypto.idCrypto)
                .reduce((sum, tx) => sum + (tx.quantite || 0), 0);

            const totalQuantiteVendus = ventes
                .filter(tx => tx.idCrypto === crypto.idCrypto)
                .reduce((sum, tx) => sum + (tx.quantite || 0), 0);
                return {
                ...crypto,
                quantite: Math.max(totalQuantiteAchat - totalQuantiteVendus, 0) // Empêche d'avoir une quantité négative
            };
        });
        portefeuille.sort((a, b) => b.quantite - a.quantite);
        console.log("Portefeuille généré :", JSON.stringify(portefeuille));
        return portefeuille;
    } catch (error) {
        console.error("Erreur lors de la récupération du portefeuille :", error);
        return [];
    }
};
