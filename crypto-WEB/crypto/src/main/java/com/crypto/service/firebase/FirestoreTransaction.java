package com.crypto.service.firebase;

import org.springframework.stereotype.Service;

import com.crypto.model.firebase.AchatVente;
import com.crypto.service.firebase.init.FirebaseInitializer;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Service
public class FirestoreTransaction {
    
    AchatVente achatVente ;
    FirebaseInitializer firebaseInitializer ; 

    public void setFirebaseInitializer() throws Exception{

        setFirebaseInitializer(new FirebaseInitializer());
        getFirebaseInitializer().initialize();
    }

    public FirestoreTransaction() throws Exception{
        setFirebaseInitializer();
        
    }

    void synchroniserTransaction() throws Exception {
         try {

            Firestore db = getFirebaseInitializer().getFirestore();

            // Référence à une collection et ajout de données
            CollectionReference  collectionRef = db.collection("portefeuille") ;
            ApiFuture<DocumentReference> result = collectionRef.add(achatVente.convertirEnMap());

            System.out.println("Document ajouté avec l'ID : " + result.get().getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw e ; 
        }
    } 

    public void synchroniser() throws Exception{
        synchroniserTransaction(); 
    } 

}
