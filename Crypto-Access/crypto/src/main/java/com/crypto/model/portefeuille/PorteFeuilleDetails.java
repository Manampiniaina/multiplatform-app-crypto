package com.crypto.model.portefeuille;

import com.crypto.model.crypto.Cryptomonnaie;

import java.sql.*;

public class PorteFeuilleDetails {
    private String id;
    private int quantite;
    private Cryptomonnaie cryptomonnaie;

    public PorteFeuilleDetails() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Cryptomonnaie getCryptomonnaie() {
        return cryptomonnaie;
    }

    public void setCryptomonnaie(Cryptomonnaie cryptomonnaie) {
        this.cryptomonnaie = cryptomonnaie;
    }


    public static PorteFeuilleDetails getById(String id, Connection connection) throws SQLException {
        String query = """
            SELECT pd.*, c.id AS cryptoId, c.*
            FROM portefeuille_detail pd
            INNER JOIN cryptomonnaie c ON pd.idCryptomonnaie = c.id
            WHERE pd.id = ?;
            """;
        PorteFeuilleDetails detail = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Cryptomonnaie cryptomonnaie = new Cryptomonnaie(
                            rs.getString("cryptoid"),
                            rs.getString("nom"),
                            rs.getDouble("d_valeur")
                    );
                    detail = new PorteFeuilleDetails();
                    detail.setId(rs.getString("id"));
                    detail.setCryptomonnaie(cryptomonnaie);
                    detail.setQuantite(rs.getInt("d_quantite"));

                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
        return detail;
    }

    public PorteFeuilleDetails update(Connection connection) throws SQLException {
        String query = "UPDATE portefeuille_detail SET d_quantite = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, this.getQuantite());
            statement.setString(2, this.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return this;
            } else {
                throw new SQLException("Aucune ligne n'a été mise à jour. L'ID spécifié n'existe pas.");
            }
        }
    }

    public void insert(Connection connection,PorteFeuille pt) throws SQLException {
        String query = "INSERT INTO portefeuille_detail(id,d_quantite,idPortefeuille,idCryptomonnaie) VALUES(default,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, this.getQuantite());
            statement.setString(2, pt.getId());
            statement.setString(3, this.getCryptomonnaie().getId());
            int rowsAffected = statement.executeUpdate();
        }
    }

}