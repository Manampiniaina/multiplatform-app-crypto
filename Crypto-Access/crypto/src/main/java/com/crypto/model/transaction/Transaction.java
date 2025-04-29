package com.crypto.model.transaction;

import com.crypto.model.crypto.Cryptomonnaie;
import com.crypto.model.crypto.TransactionCrypto;
import com.crypto.model.utilisateur.Utilisateur;
import com.crypto.service.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Transaction {

    List<TransactionCrypto> vente;
    List<TransactionCrypto> achat;

    public void addTransactionCrypto(TransactionCrypto t) {
        if(t.estAchat()) {
            this.getAchat().add(t);
        }
        else {
            this.getVente().add(t);
        }
    }

    public void setHistoriqueTransactionsByCriteria(Connection connection, String dateHeureMin, String dateHeureMax, String idUtilisateur, String idCryptomonnaie) throws Exception {
        String query = "select * from v_transaction_crypto_utilisateur_crypto where 1=1";
        if(dateHeureMin!=null && !dateHeureMin.isEmpty()) {
            query += " and dateTransaction >= (?)";
        }
        if(dateHeureMax!=null && !dateHeureMax.isEmpty()) {
            query += " and dateTransaction <= (?)";
        }
        if(idUtilisateur!=null && !idUtilisateur.isEmpty()) {
            query += " and (idVendeur = (?) or idAcheteur=(?))";
        }
        if(idCryptomonnaie!=null && !idCryptomonnaie.isEmpty()) {
            query += " and idcryptomonnaie = (?)";
        }
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            int paramIndex = 1;
            if(dateHeureMin!=null && !dateHeureMin.isEmpty()) {
                statement.setTimestamp(paramIndex, Util.formatDateTimeLocalToTimestamp(dateHeureMin));
                paramIndex++;
            }
            if(dateHeureMax!=null && !dateHeureMax.isEmpty()) {
                statement.setTimestamp(paramIndex, Util.formatDateTimeLocalToTimestamp(dateHeureMax));
                paramIndex++;
            }        
            if(idUtilisateur!=null && !idUtilisateur.isEmpty()) {
                statement.setString(paramIndex, idUtilisateur); paramIndex++;
                statement.setString(paramIndex, idUtilisateur); paramIndex++;
            }
            if(idCryptomonnaie!=null && !idCryptomonnaie.isEmpty()) {
                statement.setString(paramIndex, idCryptomonnaie);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    TransactionCrypto tCrypto = new TransactionCrypto();
                    tCrypto.setId(resultSet.getString("id"));
                    tCrypto.setDateTransaction(resultSet.getTimestamp("dateTransaction").toLocalDateTime());
                    tCrypto.setQuantite(resultSet.getInt("quantite"));
                    tCrypto.setD_prixUnitaire(resultSet.getDouble("d_prixUnitaire"));
                    tCrypto.setD_commission(resultSet.getDouble("d_commission"));
                    tCrypto.setCryptomonnaie(new Cryptomonnaie(resultSet.getString("nomcryptomonnaie"), resultSet.getString("nomcryptomonnaie")));
                    tCrypto.setUtilisateur(resultSet, resultSet.getString("idacheteur"), resultSet.getString("idvendeur"));
                    this.addTransactionCrypto(tCrypto);
                }
            }
            catch(Exception e) {
                throw e;
            }
        }
        catch(Exception er) {
            throw er;
        } 
    }

    public Transaction() {
        this.achat = new ArrayList<>();
        this.vente = new ArrayList<>();
    }

    public List<TransactionCrypto> getVente() {
        return vente;
    }

    public void setVente(List<TransactionCrypto> vente) {
        this.vente = vente;
    }

    public List<TransactionCrypto> getAchat() {
        return achat;
    }

    public void setAchat(List<TransactionCrypto> achat) {
        this.achat = achat;
    }

    public static Transaction getTransactionByUtilisateur(Utilisateur u, Connection connection,LocalDateTime dateMax){
        List<TransactionCrypto> achats  = new ArrayList<>();
        List<TransactionCrypto> ventes  = new ArrayList<>();
        Transaction rep=new Transaction();
        String idAcheteur="";
        String idVendeur="";
        String query = "SELECT * FROM transactionCrypto WHERE dateTransaction <= (?) AND (idVendeur = (?) OR idAcheteur=(?))";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setTimestamp(1,Timestamp.valueOf(dateMax));
            statement.setString(2, u.getId());
            statement.setString(3, u.getId());
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    TransactionCrypto transactionCrypto=new TransactionCrypto();
                    transactionCrypto.setId(rs.getString("id"));
                    transactionCrypto.setDateTransaction(rs.getTimestamp("dateTransaction").toLocalDateTime());
                    transactionCrypto.setQuantite(rs.getInt("quantite"));
                    transactionCrypto.setD_commission(rs.getDouble("d_commission"));
                    transactionCrypto.setD_prixUnitaire(rs.getDouble("d_prixUnitaire"));
                    transactionCrypto.setCryptomonnaie(Cryptomonnaie.getById(connection,rs.getString("idCryptomonnaie")));
                    idVendeur = rs.getString("idVendeur");
                    idAcheteur = rs.getString("idAcheteur");
                    if (idVendeur != null && !idVendeur.isEmpty()) {
                        transactionCrypto.setVendeur(u);
                        ventes.add(transactionCrypto);
                    }
                    if (idAcheteur != null && !idAcheteur.isEmpty()) {
                        transactionCrypto.setAcheteur(u);
                        achats.add(transactionCrypto);
                    }

                }
            }
            rep.setAchat(achats);
            rep.setVente(ventes);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return rep;
    }
    public static double calculateTotal(List<TransactionCrypto> transactionCryptos) {
        double total = 0;
        for (TransactionCrypto transactionCrypto : transactionCryptos) {
            total += transactionCrypto.getQuantite()*transactionCrypto.getD_prixUnitaire();
        }
        return total;
    }


}
