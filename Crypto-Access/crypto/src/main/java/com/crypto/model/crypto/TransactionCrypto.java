package com.crypto.model.crypto;

import com.crypto.exception.vente.QuantitéInsuffisanteException;
import com.crypto.model.utilisateur.Utilisateur;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.time.LocalDateTime;

@NoArgsConstructor
public class TransactionCrypto {
    
    String id ;
    LocalDateTime dateTransaction;
    int quantite;
    double d_prixUnitaire;
    double d_commission;
    Cryptomonnaie cryptomonnaie;
    Utilisateur acheteur;
    Utilisateur vendeur ;

    public void setUtilisateur(ResultSet resultSet, String idAcheteur, String idVendeur) throws Exception {
        if(idAcheteur!=null && !idAcheteur.isEmpty()) {
            this.setAcheteur(new Utilisateur(resultSet.getString("idacheteur"), resultSet.getString("nomacheteur"), resultSet.getString("prenomAcheteur"), resultSet.getDate("datenaissanceacheteur"), resultSet.getString("mailacheteur"), resultSet.getString("lienimageacheteur")));
        }
        else if(idVendeur!=null && !idVendeur.isEmpty()) {
            this.setVendeur(new Utilisateur(resultSet.getString("idvendeur"), resultSet.getString("nomvendeur"), resultSet.getString("prenomvendeur"), resultSet.getDate("datenaissancevendeur"), resultSet.getString("mailvendeur"), resultSet.getString("lienimagevendeur")));  
        }
    }

    public boolean estAchat() {
        if(this.getAcheteur()!=null) {
            return true;
        }
        return false;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(LocalDateTime dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite,int quantiteAvendre) throws QuantitéInsuffisanteException{
        if(quantite >= quantiteAvendre){
            this.quantite = quantiteAvendre;
        }else {
            throw new QuantitéInsuffisanteException("Quantite insuffisante pour effectuer la vente");
        }
    }
    public void setQuantite(int quantite) {
            this.quantite = quantite;
    }

    public double getD_prixUnitaire() {
        return d_prixUnitaire;
    }

    public void setD_prixUnitaire(double d_prixUnitaire) {
        this.d_prixUnitaire = d_prixUnitaire;
    }

    public double getD_commission() {
        return this.d_commission;
    }
    public void setD_commission(double d_commission) {
        this.d_commission = d_commission;
    }

    public void setD_commission() {
        this.d_commission = this.getCryptomonnaie().getValeur()*(this.getCryptomonnaie().getCommission().getPourcentage()/100)*this.getQuantite();
    }

    public Cryptomonnaie getCryptomonnaie() {
        return cryptomonnaie;
    }

    public void setCryptomonnaie(Cryptomonnaie cryptomonnaie) {
        this.cryptomonnaie = cryptomonnaie;
    }

    public Utilisateur getAcheteur() {
        return acheteur;
    }

    public void setAcheteur(Utilisateur acheteur) {
        this.acheteur = acheteur;
    }

    public Utilisateur getVendeur() {
        return vendeur;
    }

    public void setVendeur(Utilisateur vendeur) {
        this.vendeur = vendeur;
    }
    public void insert(Connection conn) throws Exception {
        String query = """
    INSERT INTO transactionCrypto (id, quantite, dateTransaction, d_prixUnitaire, idCryptomonnaie, idVendeur, idAcheteur, d_commission)
    VALUES (default, ?, ?, ?, ?, ?, ?, ?)
    RETURNING id;
    """;
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, this.getQuantite());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(this.getDateTransaction()));
            preparedStatement.setDouble(3, this.getCryptomonnaie().getValeur());
            preparedStatement.setString(4, this.getCryptomonnaie().getId());

            if (this.getVendeur() != null) {
                preparedStatement.setString(5, this.getVendeur().getId());
            } else {
                preparedStatement.setNull(5, java.sql.Types.VARCHAR);
            }

            if (this.getAcheteur() != null) {
                preparedStatement.setString(6, this.getAcheteur().getId());
            } else {
                preparedStatement.setNull(6, java.sql.Types.VARCHAR);
            }
            preparedStatement.setDouble(7, this.getD_commission());

            // Exécution de l'insert et récupération de l'ID généré
            try (ResultSet generatedKeys = preparedStatement.executeQuery()) {
                if (generatedKeys.next()) {
                    this.setId(generatedKeys.getString("id"));  // ID généré en tant que String
                } else {
                    throw new SQLException("Aucun ID généré");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
