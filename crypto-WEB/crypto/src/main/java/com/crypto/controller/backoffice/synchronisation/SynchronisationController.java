package com.crypto.controller.backoffice.synchronisation;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.service.connection.UtilDB;
import com.crypto.service.firebase.FirestoreRecuperation;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;

@RequestMapping("/")
@RestController
public class SynchronisationController {

    @Autowired 
    UtilDB utilDB ; 

    @Autowired
    FirestoreRecuperation firestoreRecuperation ;

    @GetMapping("synchronisation")
    public String synchronisation(Model model) {

        // Map<String, Object> maps = new HashMap<>() ;       
        // maps.put("idCryptomonnaie", "USR000000013");       
        // maps.put("idUtilisateur", "CRYPTO000000001"); 
        
        // inserer(maps, "cryptoFavori");

        // maps.put("operation", "INSERT");
        // maps.put("dateChangement", Timestamp.now()); 

        // inserer(maps, "historiqueCrypto");

        // update();

        System.out.println("Appel de synchronisation de données des utilisateurs");
        model.addAttribute("message", "Mise à jour effectuée");
        try(Connection connection = utilDB.getConnection()) {
            firestoreRecuperation.synchroniser(connection) ;

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", e.getMessage());
        }
        return "pages/frontoffice/utilisateur/connection"; 
    }

    void update(){
        // Exemple de données à insérer
            Map<String, Object> utilisateurData = new HashMap<>();
            utilisateurData.put("idUtilisateur", "USR000000013");
            utilisateurData.put("lienImage", "image.jpg");

            Map<String, Object> operationData = new HashMap<>();
            operationData.put("operation", "UPDATE");
            operationData.put("dateChangement", Timestamp.now());

            // Ajouter le document principal dans la collection `historiqueOperationUtilisateur`
            DocumentReference docRef = firestoreRecuperation.getFirebaseInitializer().getFirestore().collection("historiqueOperationUtilisateur").document();
            ApiFuture<WriteResult> result = docRef.set(operationData);

            // Ajouter le sous-document `utilisateur` avec l'ID `utilisateurId` dans le document principal
            DocumentReference utilisateurRef = docRef.collection("utilisateur").document("utilisateurId");
            ApiFuture<WriteResult> utilisateurResult = utilisateurRef.set(utilisateurData);

            try {
                System.out.println("Documents insérés avec succès : " + result.get().getUpdateTime() + ", " + utilisateurResult.get().getUpdateTime());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
    }

    void inserer(Map<String, Object> map, String collection) {
        
        DocumentReference docRef = firestoreRecuperation.getFirebaseInitializer().getFirestore().collection(collection).document();
        ApiFuture<WriteResult> result = docRef.set(map);

        try {
            System.out.println("Documents insérés avec succès : " + result.get().getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
