import { db } from "../configuration/FirebaseConfig"; // Import de la connexion Firebase
import { collection, getDocs, getFirestore, doc, getDoc } from "firebase/firestore";

// Fonction pour récupérer toutes les cryptomonnaies
export const getAllCryptos = async () => {
    try {
        const db = getFirestore(); // 🔥 Assure-toi que Firestore est bien initialisé
        const objsRef = collection(db, "cryptomonnaies");

        const objsSnapshot = await getDocs(objsRef);
        const objs = objsSnapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));
//        console.log("Tous les Cryptos :", JSON.stringify(objs, null, 2));
        return objs;
    } catch (error) {
        console.error("Erreur lors de la récupération des cryptomonnaies:", error);
        return [];
    }
};

// Fonction pour récupérer une cryptomonnaie par ID depuis Firestore
export const getCryptoById = async (id) => {
    try {
        const db = getFirestore();
        const cryptoRef = doc(db, "cryptomonnaies", id);
        const cryptoSnap = await getDoc(cryptoRef);

        if (cryptoSnap.exists()) {
            //console.log("Crypto trouvée :", JSON.stringify(cryptoSnap.data(), null, 2));
            return { id: cryptoSnap.id, ...cryptoSnap.data() };
        } else {
            console.log("Aucune cryptomonnaie trouvée pour l'ID:", id);
            return null;
        }
    } catch (error) {
        console.error("Erreur lors de la récupération de la cryptomonnaie:", error);
        return null;
    }
};

// Fonction pour récupérer une cryptomonnaie par ID en la cherchant dans une liste de cryptos
export const getCryptoByIdFromList = (id, cryptos) => {
    const crypto = cryptos.find(crypto => crypto.idCrypto === id);
    if (crypto) {
       // console.log("Crypto trouvée dans la liste:", JSON.stringify(crypto, null, 2));
        return crypto;
    } else {
        console.log("Aucune cryptomonnaie trouvée dans la liste pour l'ID:", id);
        return null;
    }
};

const images = {
    Bitcoin: require('../../assets/btc-logo.webp'),
    Ethereum: require('../../assets/Ethereum.png'),
    Cardano: require('../../assets/Cardano.webp'),
    Solana: require('../../assets/Solana.webp'),
    Dogecoin: require('../../assets/Dogecoin.webp'),
    "Shiba Inu": require('../../assets/ShibaInu.webp'),
    Polkadot: require('../../assets/Polkadot.webp'),
    Avalanche: require('../../assets/Avalanche.webp'),
    XRP: require('../../assets/XPR.webp'),
    Chainlink : require('../../assets/Chainlink.webp'),
    default: require('../../assets/crypto.png') ,

};

export const getImagePath = (crypto) => images[crypto] || images.default;
