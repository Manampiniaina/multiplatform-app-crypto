package com.crypto.model.firebase;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.crypto.model.utilisateur.Utilisateur;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HistoriqueOperationUtilisateur {

    String id;
    Utilisateur utilisateur;
    String operation;
    Timestamp dateChangement;

    public HistoriqueOperationUtilisateur(String id, Utilisateur utilisateur, String operation, Timestamp dateChangement) {
        setId(id);
        setUtilisateur(utilisateur);
        setOperation(operation);
        setDateChangement(dateChangement);
    }


    // void update(Connection connection) throws Exception {
    //     getUtilisateur().update(connection) ; 
    // }

    public static void update(Connection connection, HistoriqueOperationUtilisateur[] historiqueOperationUtilisateurs) throws Exception {

        List<Utilisateur> utilisateurs = new ArrayList<>();
        for (HistoriqueOperationUtilisateur historiqueOperationUtilisateur : historiqueOperationUtilisateurs) {
            if(historiqueOperationUtilisateur.getOperation().equalsIgnoreCase("UPDATE")) utilisateurs.add(historiqueOperationUtilisateur.getUtilisateur());
        }
        try {
            Utilisateur.update(connection, utilisateurs.toArray(new Utilisateur[0]));
        } catch (Exception e) {
            e.printStackTrace();
            throw e ;
        } 
        
    }

    @Override
    public String toString() {
        return "HistoriqueOperationUtilisateur{" +
                "id='" + id + '\'' +
                ", operation='" + operation + '\'' +
                ", dateChangement=" + dateChangement +
                ", utilisateur=" + utilisateur +
                '}';
    }
}
