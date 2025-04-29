package com.crypto.model.achat;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.crypto.exception.model.ValeurInvalideException;

public class Achat {
    private String id ;
    private Integer quantityAchat  ;
    private Date dateAchat;
    private String idAcheteur;
    private String idVente;

    public Achat() {    }
    public Achat(String id) { setId(id);   }
    public Achat(String id, Integer quantityAchat, Date dateAchat, String idAcheteur, String idVente) throws ValeurInvalideException{
        setId(id);
        setQuantityAchat(quantityAchat);
        setDateAchat(dateAchat);
        setIdAcheteur(idAcheteur);
        setIdVente(idVente);
    }

    public Achat(Integer quantityAchat, Date dateAchat, String idAcheteur, String idVente) throws ValeurInvalideException{
        setQuantityAchat(quantityAchat);
        setDateAchat(dateAchat);
        setIdAcheteur(idAcheteur);
        setIdVente(idVente);
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public Integer getQuantityAchat() {
        return quantityAchat;
    }

    public void setQuantityAchat(Integer quantityAchat) throws ValeurInvalideException{
        if(quantityAchat<=0) throw new ValeurInvalideException("Quantité acheté invalide");
        else this.quantityAchat = quantityAchat;
    }

    public Date getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(Date dateAchat) {
        this.dateAchat = dateAchat;
    }


    public String getIdAcheteur() {
        return idAcheteur;
    }

    public void setIdAcheteur(String idAcheteur) {
        this.idAcheteur = idAcheteur;
    }

    public String getIdVente() {
        return idVente;
    }

    public void setIdVente(String idVente) {
        this.idVente = idVente;
    }
    public Achat insert(Connection connection) throws Exception {
        String query = "INSERT INTO Achat (id, quantiteachat, dateachat, idacheteur, idvente) " +
                "VALUES (DEFAULT, ?, ?, ?, ?)";

                System.out.println(getIdVente());
        connection.setAutoCommit(false);

        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, this.getQuantityAchat());
            statement.setDate(2, this.getDateAchat());
            statement.setString(3, this.getIdAcheteur());
            statement.setString(4, this.getIdVente());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("L'insertion a échoué, aucune ligne affectée.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    String generatedId = generatedKeys.getString(1);
                    this.setId(generatedId);
                } else {
                    throw new SQLException("L'insertion a échoué, aucun ID généré.");
                }
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }

        return this;
    }


    public static List<Achat> findAll(Connection connection) throws Exception {
        List<Achat> achats = new ArrayList<>();
        String query = "SELECT * FROM achat"; // Table `achat` dans la base de données
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Achat achat = new Achat(
                        rs.getString("id") ,
                        rs.getInt("quantiteachat"),
                        rs.getDate("dateachat"),
                        rs.getString("idacheteur"),
                        rs.getString("idvente")
                );
                achats.add(achat);
            }
        }
        return achats;
    }

    public Achat findById(Connection connection) throws Exception {
        String query = "SELECT * FROM achat WHERE id=?"; // Table `achat` dans la base de données
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setString(1 , this.getId());
             try(ResultSet rs = stmt.executeQuery()) {
                 while (rs.next()) {
                     Achat achat = new Achat(
                             rs.getString("id"),
                             rs.getInt("quantiteachat"),
                             rs.getDate("dateachat"),
                             rs.getString("idacheteur"),
                             rs.getString("idvente")
                     );
                     return achat;
                 }
             }
        }
       return null;
    }

    // public void modifierPorteFeuille(Connection connection) throws Exception{
    //     Vente vente = Vente.getById(connection, getIdVente());

    //     PorteFeuille porteFeuille = PorteFeuille.getByIdUtilisateur(getIdAcheteur() , connection);
    //     assert porteFeuille != null;

    //     List<PorteFeuilleDetails> details = PorteFeuilleDetails.getPorteFeuilleDetailsByPorteFeuille(porteFeuille.getId() , connection);
    //     PorteFeuilleDetails detailAcheteur = new PorteFeuilleDetails();
    //     for (PorteFeuilleDetails detail : details) {
    //         if(detail.getId().equals(vente.getPortefeuilleDetail().getId())) {
    //             detailAcheteur = detail;
    //             break;
    //         }
    //     }
    //     PorteFeuilleDetails detailVendeur =vente.getPortefeuilleDetail();

    //     detailAcheteur.setQuantite(detailAcheteur.getQuantite()+(getQuantityAchat()) );
    //     detailVendeur.setQuantite(detailVendeur.getQuantite()-(getQuantityAchat()) );

    //     detailVendeur.update(connection);
    //     detailAcheteur.update(connection);
    //     // System.out.println(detailAcheteur.getQuantite());
    // }
}
