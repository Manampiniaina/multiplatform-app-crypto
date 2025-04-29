package com.crypto.service.firebase;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.sql.Connection ;

import org.springframework.stereotype.Service;

import com.crypto.model.utilisateur.Utilisateur;
import com.crypto.service.firebase.init.FirebaseInitializer;
import com.crypto.model.fond.Fond;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter 
@Service
public class FirestoreUtilisateur {

    Utilisateur utilisateur ;
    FirebaseInitializer firebaseInitializer ; 

    public void setFirebaseInitializer() throws Exception{

        setFirebaseInitializer(new FirebaseInitializer());
        getFirebaseInitializer().initialize();
    }

    public FirestoreUtilisateur() throws Exception{
        setFirebaseInitializer();
    }

    public FirestoreUtilisateur(Utilisateur utilisateur) throws Exception{
        try {
            setFirebaseInitializer();
            setUtilisateur(utilisateur);
        } catch (Exception e) {
            e.printStackTrace();
        }
       
    }

    public void synchroniserFond(Connection connection) throws Exception{

        Firestore db = getFirebaseInitializer().getFirestore();
        double montant = (Fond.getFondByUtilisateur(getUtilisateur(), connection)).getMontant() ;
        Map<String, Object> map = new HashMap<>() ;
        map.put("utilisateur", getUtilisateur().getId());
        map.put("montant", montant);

        // Référence à une collection et ajout de données
        CollectionReference  collectionRef = db.collection("fond") ;
        ApiFuture<DocumentReference> result = collectionRef.add(map);
         
    }

    public void envoyerFond(Map<String, Object> map, String collection) throws Exception {
        
        DocumentReference docRef = getFirebaseInitializer().getFirestore().collection(collection).document();
        ApiFuture<WriteResult> result = docRef.set(map);

        try {
            System.out.println("Documents insérés avec succès : " + result.get().getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw e ;
        }
    }

    void ajouterUtilisateur(Firestore db) throws Exception {

        Map<String, Object> map = new HashMap<>() ;
        map.put("dateChangement", Timestamp.valueOf(LocalDateTime.now()));
        map.put("operation", "INSERT");
        
        // Exemple de données à envoyer
        Map<String, Object> data = new HashMap<>();
        data.put("id", getUtilisateur().getId());
        data.put("nom", getUtilisateur().getNom());
        data.put("prenom", getUtilisateur().getPrenom());
        data.put("mail", getUtilisateur().getMail());
        data.put("date_naissance", getUtilisateur().getDateNaissance());
        data.put("lienImage", getUtilisateur().getLienImage());
        data.put("creation", new java.util.Date().toString());

        map.put("utilisateur", data) ;
        // Référence à une collection et ajout de données
        CollectionReference  collectionRef = db.collection("historiqueOperationUtilisateur") ;
        ApiFuture<DocumentReference> result = collectionRef.add(map);


    }

    void synchroniserUtilisateur(Firestore db) throws Exception{
        
        try {

            // Exemple de données à envoyer
            Map<String, Object> data = new HashMap<>();
            data.put("id", getUtilisateur().getId());
            data.put("nom", getUtilisateur().getNom());
            data.put("prenom", getUtilisateur().getPrenom());
            data.put("mail", getUtilisateur().getMail());
            data.put("date_naissance", getUtilisateur().getDateNaissance());
            data.put("lienImage", getUtilisateur().getLienImage());
            data.put("creation", new java.util.Date().toString());

            // Référence à une collection et ajout de données
            CollectionReference  collectionRef = db.collection("utilisateurs") ;
            ApiFuture<DocumentReference> result = collectionRef.add(data);

            ajouterUtilisateur(db);

            System.out.println("Document ajouté avec l'ID : " + result.get().getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw e ; 
        }
    }

    UserRecord synchroniserAuthentification(Firestore db) throws Exception {
        CreateRequest request = new CreateRequest()
            .setEmail(getUtilisateur().getMail())
            .setPassword(getUtilisateur().getMdp());

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        return userRecord;
    }

    public void synchroniser() throws Exception {

        System.out.println("Valeur de utilisateur "+getUtilisateur().toString());
        Firestore db = getFirebaseInitializer().getFirestore();
        synchroniserAuthentification(db) ;
        synchroniserUtilisateur(db);
    }
}

