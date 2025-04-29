package com.crypto.model.utilisateur.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.crypto.exception.authentification.ConnectionEchoueException;
import com.crypto.model.fond.Fond;
import com.crypto.model.fond.MouvementFondAttente;
import com.crypto.model.utilisateur.Utilisateur;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Admin {

    String id;
    String nom;
    String mdp;

    public void validerDemandeFond(Connection connection, MouvementFondAttente mvt) throws Exception {
        try {
            if(mvt.estDepot()) {
                mvt.transfererVersFond(connection);
            }
            else {
                Utilisateur utilisant = mvt.getUtilisateur();
                utilisant.setFond(Fond.getFondByUtilisateur(utilisant, connection));
                utilisant.checkValeurRetrait(mvt);
                mvt.transfererVersFond(connection);
            }
        }
        catch(Exception e) {
            throw e;
        }
    }

    // Constructeur
    public Admin(String nom, String mdp) {
        setNom(nom);
        setMdp(mdp);
    }

    public Admin(String id, String nom, String mdp) {
        setId(id);
        setNom(nom);
        setMdp(mdp);
    }


    public void setByNom(Connection connection) throws Exception{

        String query = "SELECT * FROM admin WHERE nom = ? and mdp = MD5(?)";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, getNom());
            statement.setString(2, getMdp());

            try (ResultSet resultSet = statement.executeQuery()) {
                
                if (resultSet.next()) {
                   setId(resultSet.getString("id"));
                } else {
                    throw new ConnectionEchoueException("Vérifier votre nom et votre mot de passe");
                }
            }
        }
    }

    public void se_connecter(Connection connection) throws Exception {
        setByNom(connection) ;
    }

    // Méthode toString
    @Override
    public String toString() {
        return "Admin{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", mdp='" + mdp + '\'' +
                '}';
    }
}
