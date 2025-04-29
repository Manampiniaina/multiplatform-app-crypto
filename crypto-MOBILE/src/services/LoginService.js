import { getByMail } from "./UtilisateurService";
import { getAllCryptos } from "./CryptosService";
import {signInWithEmailAndPassword} from "firebase/auth";
import {auth} from "../configuration/FirebaseConfig";

export const handleLogin = async (
    email,
    password,
    loginUser,
    navigation,
    setEmailError,
    setPasswordError,
    setIsInitializing
) => {
    try {
        // Perform login validation
        if (!email) {
            setEmailError('Email est requis');
            return;
        }
        if (!password) {
            setPasswordError('Mot de passe est requis');
            return;
        }

        // Simulate login (replace with actual authentication)
        const userCredential = await signInWithEmailAndPassword(auth, email, password);
        const userData = userCredential.user;
        // Initialize user context
        await loginUser(userData);

        // Fetch additional user data
        const userFromDB = await getByMail(email);

        // Ensure cryptos are loaded (optional, depending on your requirements)
        await getAllCryptos();


        // Navigate to main screen after successful initialization
        navigation.replace('Main');
    } catch (error) {
        console.error('Login error:', error);

        switch (error.code) {
            case "auth/wrong-password":
                setPasswordError("Mot de passe incorrect.");
                break;
            case "auth/invalid-credential":
                setPasswordError(" Mot de passe incorrect. OU Email Inexistant ");
                break;
            case "auth/user-not-found":
                setEmailError("Email inexistant.");
                break;
            case "auth/invalid-email":
                setEmailError("Format d'email invalide.");
                break;
            case "auth/network-request-failed":
                setEmailError("Échec de connexion : Vérifiez votre connexion Internet.");
                break;
            case "auth/too-many-requests":
                Alert.alert("Trop de tentatives", "Veuillez réessayer plus tard.");
                break;
            default:
                Alert.alert("Échec de la connexion", "Une erreur est survenue.");
}
        } finally {
        // Ensure initialization state is reset
        if (setIsInitializing) {
            setIsInitializing(false);
        }
    }
};