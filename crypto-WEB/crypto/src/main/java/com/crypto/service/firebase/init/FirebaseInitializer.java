package com.crypto.service.firebase.init;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FirebaseInitializer {

    @PostConstruct
    public void initialize() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) { // Vérification pour éviter l'erreur
            // ClassPathResource serviceAccount = new ClassPathResource("crytpo-f999a-firebase-adminsdk-fbsvc-dadebf6ef4.json");
            // ClassPathResource serviceAccount = new ClassPathResource("first-firebase-5ca70-firebase-adminsdk-rnsw9-9e05a112bf.json");
            ClassPathResource serviceAccount = new ClassPathResource("mobile-crypto-a22eb-firebase-adminsdk-fbsvc-a8dd1b7235.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("✅ Firebase a été initialisé avec succès !");
        } else {
            System.out.println("⚠️ Firebase est déjà initialisé, aucune action requise.");
        }
    }

    public Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }
}
