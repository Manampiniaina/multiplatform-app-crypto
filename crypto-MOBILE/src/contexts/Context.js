import React, {createContext, useContext, useEffect, useState} from 'react';
import {getAllCryptos} from "../services/CryptosService";
import {getByMail} from "../services/UtilisateurService";
import {getFond} from "../services/FondService";

const UserContext = createContext();
const CryptoContext = createContext();

export const useUser = () => useContext(UserContext);
export const useCryptos = () => useContext(CryptoContext);

export const UserProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [utilisateur, setUtilisateur] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    const loginUser = async (userData) => {
        setUser(userData);
        console.log("mail:" + userData.email);

        const userFromDB = await getByMail(userData.email);

        setUtilisateur(userFromDB);

        setIsLoading(false);
    };

    const logoutUser = () => {
        setUser(null);
        setIsLoading(false);
    };

    useEffect(() => {
        // Simulation d'un utilisateur déjà connecté (si besoin)
        setTimeout(() => {
            setIsLoading(false); // Supprime le loading même si pas d'utilisateur
        }, 1000);
    }, []);

    return (
        <UserContext.Provider value={{ user, utilisateur, loginUser, logoutUser, isLoading }}>
            {children}
        </UserContext.Provider>
    );
};
export const CryptoProvider = ({ children }) => {
    const [cryptos, setCryptos] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const { utilisateur } = useUser(); // Vérifier si l'utilisateur est chargé

    useEffect(() => {
        let interval;

        const fetchCryptos = async () => {
            const data = await getAllCryptos();
            setCryptos(data);
            setIsLoading(false);
        };

        // Démarrer l'intervalle seulement si l'utilisateur est initialisé
        if (utilisateur) {
            fetchCryptos(); // Charger une première fois
            interval = setInterval(fetchCryptos, 10000); // Actualisation toutes les 10s
        }

        return () => {
            if (interval) clearInterval(interval);
        };
    }, [utilisateur]); // Lancer la mise à jour seulement après l'initialisation de l'utilisateur

    return (
        <CryptoContext.Provider value={{ cryptos, setCryptos, isLoading }}>
            {children}
        </CryptoContext.Provider>
    );
};

