package com.crypto.model.vente;

import com.crypto.model.portefeuille.PorteFeuilleDetails;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Vente {
    String id;
    int quantiteVendu;
    Date dateVente;
    double d_prixVente;
    PorteFeuilleDetails portefeuilleDetail;

    public Vente() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantiteVendu() {
        return quantiteVendu;
    }

    public void setQuantiteVendu(int quantiteVendu) {
        this.quantiteVendu = quantiteVendu;
    }

    public Date getDateVente() {
        return dateVente;
    }

    public void setDateVente(Date dateVente) {
        this.dateVente = dateVente;
    }

    public double getD_prixVente() {
        return d_prixVente;
    }

    public void setD_prixVente(double d_prixVente) {
        this.d_prixVente = d_prixVente;
    }

    public PorteFeuilleDetails getPortefeuilleDetail() {
        return portefeuilleDetail;
    }

    public void setPortefeuilleDetail(PorteFeuilleDetails portefeuilleDetail) {
        this.portefeuilleDetail = portefeuilleDetail;
    }
    public static List<Vente> getVenteDisponible(Connection connection) {
        List<Vente> ventesDisponibles = new ArrayList<>();
        String query = "SELECT v.id, v.quantiteVendu, v.dateVente, v.d_prixVente, v.idPortefeuilleDetail " +
                "FROM vente v " +
                "WHERE v.id NOT IN (SELECT idVente FROM achat)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Vente vente = new Vente();
                vente.setId(resultSet.getString("id"));
                vente.setQuantiteVendu(resultSet.getInt("quantiteVendu"));
                vente.setDateVente(resultSet.getDate("dateVente"));
                vente.setD_prixVente(resultSet.getDouble("d_prixVente"));

                PorteFeuilleDetails portefeuilleDetails =PorteFeuilleDetails.getById(resultSet.getString("idPortefeuilleDetail"),connection);
                vente.setPortefeuilleDetail(portefeuilleDetails);

                ventesDisponibles.add(vente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ventesDisponibles;
    }
    public static void insert(Vente vente, Connection connection) throws SQLException {
        String query = """
            INSERT INTO vente (id, quantiteVendu, dateVente, d_prixVente, idPortefeuilleDetail)
            VALUES (default, ?, ?, ?, ?);
            """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, vente.getQuantiteVendu());
            preparedStatement.setDate(2, vente.getDateVente());
            preparedStatement.setDouble(3, vente.getD_prixVente());
            preparedStatement.setString(4, vente.getPortefeuilleDetail().getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    //    public static Vente getById(Connection connection, String idVente) throws SQLException {
    //     String query = """
    //     SELECT 
    //       * 
    //     FROM 
    //         v_vente_utilisateur_detail
    //     WHERE 
    //         venteid = ?;
    //     """;

    //     try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    //         preparedStatement.setString(1, idVente);

    //         try (ResultSet resultSet = preparedStatement.executeQuery()) {
    //             if (resultSet.next()) {
    //                 Vente vente = new Vente();

    //                 // Remplissage des champs de l'objet Vente
    //                 vente.setId(resultSet.getString("venteId"));
    //                 vente.setQuantiteVendu(resultSet.getInt("quantiteVendu"));
    //                 vente.setDateVente(resultSet.getDate("dateVente"));
    //                 vente.setD_prixVente(resultSet.getDouble("d_prixVente"));

    //                 // Créer l'objet PorteFeuilleDetails et le lier à Vente
    //                 PorteFeuilleDetails portefeuilleDetails = new PorteFeuilleDetails();
    //                 portefeuilleDetails.setId(resultSet.getString("portefeuilleDetailId"));
    //                 portefeuilleDetails.setQuantite(resultSet.getInt("quantite"));

    //                 // Lier PorteFeuilleDetails à Vente
    //                 vente.setPortefeuilleDetail(portefeuilleDetails);

    //                 Utilisateur utilisateur = new Utilisateur();
    //                 utilisateur.setNom(resultSet.getString("nom"));
    //                 PorteFeuille pt=new PorteFeuille();
    //                 pt.setUtilisateur(utilisateur);
    //                 portefeuilleDetails.setPorteFeuille(pt);

    //                 Cryptomonnaie cryptomonnaie = new Cryptomonnaie();
    //                 cryptomonnaie.setValeur(resultSet.getDouble("valeur"));

    //                 portefeuilleDetails.setCryptomonnaie(cryptomonnaie);
    //                 vente.setPortefeuilleDetail(portefeuilleDetails);
    //                 return vente;
    //             }
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         throw e;
    //     }

    //     return null;
    // }

}
