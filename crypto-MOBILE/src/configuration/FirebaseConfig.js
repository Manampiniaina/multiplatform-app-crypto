import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
import { getFirestore } from "firebase/firestore";

  const firebaseConfig = {
    apiKey: "AIzaSyAkWgOb-qPFZns2h5gBzwqxz0hUQ_fbKas",
  authDomain: "mobile-crypto-a22eb.firebaseapp.com",
  projectId: "mobile-crypto-a22eb",
  storageBucket: "mobile-crypto-a22eb.firebasestorage.app",
  messagingSenderId: "962618509326",
  appId: "1:962618509326:web:3bc7fe14f0c65ec419945f"
  };

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const db = getFirestore(app);

export { auth ,db};
