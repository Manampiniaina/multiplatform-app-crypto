package com.crypto.service.firebase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.crypto.model.crypto.ChangementCoursCrypto;
import com.crypto.service.firebase.init.FirebaseInitializer;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Service
public class FirestoreCryptomonnaie {
 
    ChangementCoursCrypto[] cryptomonnaies ; 
    FirebaseInitializer firebaseInitializer ; 

    public void setFirebaseInitializer() throws Exception{

        setFirebaseInitializer(new FirebaseInitializer());
        getFirebaseInitializer().initialize();
    }

    public FirestoreCryptomonnaie() throws Exception{
        setFirebaseInitializer();
    }

    public FirestoreCryptomonnaie(ChangementCoursCrypto[] cryptomonnaies) throws Exception{
        try {
            setFirebaseInitializer();
            setCryptomonnaies(cryptomonnaies);
        } catch (Exception e) {
            e.printStackTrace();
        }
       
    }

    void synchroniserCryptomonnaie(ChangementCoursCrypto cryptomonnaie) throws Exception{
        try {

            Firestore db = getFirebaseInitializer().getFirestore();

            // Exemple de données à envoyer
            Map<String, Object> data = new HashMap<>();
            data.put("id", cryptomonnaie.getId());
            data.put("idCrypto", cryptomonnaie.getCryptomonnaie().getId());
            data.put("nom", cryptomonnaie.getCryptomonnaie().getNom());
            data.put("valeur", cryptomonnaie.getValeur());
            data.put("dateChangement", cryptomonnaie.getDate());
            data.put("creation", new java.util.Date().toString());

            // Référence à une collection et ajout de données
            CollectionReference  collectionRef = db.collection("cryptomonnaies") ;
            ApiFuture<DocumentReference> result = collectionRef.add(data);

        } catch (Exception e) {
            e.printStackTrace();
            throw e ; 
        }
    }

    public void synchroniser() throws Exception {

        try {
            supprimerContenuCollection();

            for (ChangementCoursCrypto changementCoursCrypto : getCryptomonnaies()) {
                synchroniserCryptomonnaie(changementCoursCrypto);
            }
        } catch (Exception e) {
            throw e ;
        }
        
    }

    void supprimerContenuCollection() throws Exception {
        try {
            Firestore db = getFirebaseInitializer().getFirestore();
            CollectionReference collectionRef = db.collection("cryptomonnaies");

            // Récupérer tous les documents de la collection
            ApiFuture<QuerySnapshot> future = collectionRef.get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            // Supprimer chaque document
            for (QueryDocumentSnapshot document : documents) {
                document.getReference().delete();
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
