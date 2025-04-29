package com.crypto.model.portefeuille;

import com.crypto.model.crypto.Cryptomonnaie;
import com.crypto.model.utilisateur.Utilisateur;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PorteFeuille {
    private String id;
    private Utilisateur utilisateur;
    private List<PorteFeuilleDetails> porteFeuilleDetails;

    public PorteFeuilleDetails verifier(Cryptomonnaie cryptomonnaie){
        if(this.getPorteFeuilleDetails()!=null) {
            for (PorteFeuilleDetails pt:this.getPorteFeuilleDetails()){
                if(pt.getCryptomonnaie().getId().equals(cryptomonnaie.getId())){
                    return pt;
                }
            }
        }
        return  null;
    }


    public void setPorteFeuilleDetailsByConnection(Connection connection) throws Exception {
        List<PorteFeuilleDetails> details = new ArrayList<>();
        String query = "SELECT * FROM portefeuille_detail  WHERE idPortefeuille = ?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, this.getId());
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    PorteFeuilleDetails detail = PorteFeuilleDetails.getById(rs.getString("id"),connection);
                    details.add(detail);
                }
            }
            catch(Exception e) {
                throw e;
            }
        }
        catch(Exception e) {
            throw e;
        }
        this.setPorteFeuilleDetails(details);
    }
    

    public PorteFeuille() {
    }

    public PorteFeuille(String id, Utilisateur utilisateur) {
        this.id = id;
        this.utilisateur = utilisateur;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public List<PorteFeuilleDetails> getPorteFeuilleDetails() {
        return porteFeuilleDetails;
    }

    public void setPorteFeuilleDetails(List<PorteFeuilleDetails> porteFeuilleDetails) {
        this.porteFeuilleDetails = porteFeuilleDetails;
    }

    // Méthode pour récupérer un portefeuille par l'ID de l'utilisateur
    public static PorteFeuille getByIdUtilisateur(String idUtilisateur,Connection connection ) throws SQLException {
        String query = """
                SELECT p.id, u.id AS utilisateurId, u.nom, u.prenom, u.date_naissance, u.mail
                FROM portefeuille p
                INNER JOIN utilisateur u ON p.id_idUtilisateur = u.id
                WHERE u.id = ?;
                """;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, idUtilisateur);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Utilisateur utilisateur = new Utilisateur(rs.getString("utilisateurId"));
                    utilisateur.setNom(rs.getString("nom"));
                    utilisateur.setPrenom(rs.getString("prenom"));
                    utilisateur.setDateNaissance(rs.getDate("date_naissance"));
                    utilisateur.setMail(rs.getString("mail"));

                    return new PorteFeuille(rs.getString("id"), utilisateur);
                }
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }
}
