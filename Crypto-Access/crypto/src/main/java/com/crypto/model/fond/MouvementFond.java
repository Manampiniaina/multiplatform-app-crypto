package com.crypto.model.fond;

import com.crypto.exception.model.ValeurInvalideException;
import com.crypto.model.crypto.TransactionCrypto;
import com.crypto.model.utilisateur.Utilisateur;
import com.crypto.service.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class MouvementFond {

    String id;
    double montant;
    LocalDateTime dateMouvement;
    TransactionCrypto transactionCrypto;

    public MouvementFond(String i) {
        this.setId(i);
    }

    public MouvementFond(String typeDemande, String montant, String dateDemande) throws Exception {
        this.setMontant(montant);
        this.setSigne(Integer.valueOf(typeDemande));
        this.setDateMouvement(dateDemande);
    }

    public void insererAttente(Connection connection, Utilisateur utilisateur) throws Exception {
        String query = "INSERT INTO fondAttente (montant, dateMouvement, idUtilisateur) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);

            statement.setDouble(1, this.getMontant());
            statement.setTimestamp(2, Timestamp.valueOf(this.getDateMouvement()));
            statement.setString(3, utilisateur.getId());

            statement.executeUpdate();
            connection.commit();
        }  
        catch(Exception e) {
            if(connection != null) {
                connection.rollback();
            }
            throw e;
        }
        finally {
            if(connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }

    public boolean estDepot() {
        if(this.getMontant() > 0) {
            return true;
        }
        return false;
    }

    public MouvementFond() {
    }

    public MouvementFond(double montant, LocalDateTime dateMouvement, TransactionCrypto transactionCrypto) throws ValeurInvalideException {
        setMontant(montant);
        setDateMouvement(dateMouvement);
        setTransactionCrypto(transactionCrypto);

    }

    public MouvementFond(String id, double montant, LocalDateTime dateMouvement, TransactionCrypto transactionCrypto) throws ValeurInvalideException {
        setId(id);
        setMontant(montant);
        setDateMouvement(dateMouvement);
        setTransactionCrypto(transactionCrypto);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(String montant) throws ValeurInvalideException {
        try {
            double mtt = Double.valueOf(montant);
            this.setMontant(mtt);
        }   
        catch(Exception e) {
            throw new ValeurInvalideException("Valeur de montant demandé invalide");
        }
    }

    public void setMontant(double montant) throws ValeurInvalideException{
        if(montant<0) throw new ValeurInvalideException("Valeur de montant demandé invalide") ;
        else this.montant=montant;
    }

    public void setMontantSansControl(double montant){

        this.montant = montant ;
    }

    public void setSigne(int signe) {
        this.montant = this.montant * signe;
    } 

    public LocalDateTime getDateMouvement() {
        return dateMouvement;
    }

    public void setDateMouvement(String dateDemande) throws ValeurInvalideException {
        try {
            String formattedDateDmd = dateDemande.replace("T", " ") + ":00";
            // System.out.println("\n date demande formatée : "+formattedDateDmd+"\n");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime date = LocalDateTime.parse(formattedDateDmd, formatter);       
            this.setDateMouvement(date);
        } catch (Exception e) {
            throw new ValeurInvalideException("Valeur de la date de demande invalide");
        }
    }

    public void setDateMouvement(LocalDateTime dateMouvement) {
        this.dateMouvement = dateMouvement;
    }

    public TransactionCrypto getTransactionCrypto() {
        return transactionCrypto;
    }

    public void setTransactionCrypto(TransactionCrypto transactionCrypto) {
        this.transactionCrypto = transactionCrypto;
    }

    public void insert(Connection conn, Utilisateur u) throws Exception {
        String query = """
        INSERT INTO fond (id, montant, dateMouvement, idTransactionCrypto, idUtilisateur)
        VALUES (default, ?, ?, ?, ?);
        """;
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setDouble(1, this.getMontant());
            preparedStatement.setTimestamp(2, Util.getDateTimeActuelle());
            if (this.getTransactionCrypto() != null) {
                preparedStatement.setString(3, this.getTransactionCrypto().getId());
            } else {
                preparedStatement.setNull(3, java.sql.Types.VARCHAR);
            }
            preparedStatement.setString(4,u.getId() );

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
