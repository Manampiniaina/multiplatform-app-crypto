// package com.crypto.service.firebase;

// import com.crypto.service.firebase.init.FirebaseInitializer;
// import com.google.api.core.ApiFuture;
// import com.google.cloud.firestore.DocumentReference;
// import com.google.cloud.firestore.Firestore;
// import com.google.cloud.firestore.WriteResult;

// import java.util.HashMap;
// import java.util.Map;

// public class FirestoreExemple {

//     public static void main(String[] args) {
//         try {
//             FirebaseInitializer.initialize();

//             Firestore db = FirebaseInitializer.getFirestore();

//             // Exemple de données à envoyer
//             Map<String, Object> data = new HashMap<>();
//             data.put("name", "John Doe");
//             data.put("timestamp", new java.util.Date().toString());

//             // Référence à une collection et ajout de données
//             DocumentReference docRef = db.collection("users").document("user_123");
//             ApiFuture<WriteResult> result = docRef.set(data);

//             System.out.println("Données envoyées avec succès : " + result.get().getUpdateTime());
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }
