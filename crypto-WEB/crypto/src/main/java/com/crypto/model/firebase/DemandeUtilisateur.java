package com.crypto.model.firebase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.crypto.model.fond.MouvementFond;
import com.crypto.model.utilisateur.Utilisateur;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
@NoArgsConstructor
public class DemandeUtilisateur {
    
    Utilisateur utilisateur ; 
    MouvementFond mouvementFond ;


    public DemandeUtilisateur(Utilisateur utilisateur,MouvementFond mouvementFond) {
        setUtilisateur(utilisateur);
        setMouvementFond(mouvementFond);
    }

    public static void inserer(Connection connection, DemandeUtilisateur[] demandeUtilisateurs) throws Exception {
        
        String query = "INSERT INTO fondAttente(id, montant, dateMouvement, idUtilisateur) VALUES (default, ?, ?, ?)";
        
        System.out.println("Longueur des données à insérer "+demandeUtilisateurs.length);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
    
            for (DemandeUtilisateur dmd : demandeUtilisateurs) {

                statement.setDouble(1, dmd.getMouvementFond().getMontant());
                statement.setTimestamp(2, Timestamp.valueOf(dmd.getMouvementFond().getDateMouvement()));
                statement.setString(3, dmd.getUtilisateur().getId());
                statement.addBatch(); 
            }
    
            int[] affectedRows = statement.executeBatch();
    
            for (int count : affectedRows) {
                if (count == 0) {
                    throw new SQLException("Échec de la mise à jour pour au moins une cryptomonnaie.");
                }
            }
        }
            
    }
}
