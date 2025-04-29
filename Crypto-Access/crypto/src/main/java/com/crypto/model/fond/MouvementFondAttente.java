package com.crypto.model.fond;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.crypto.model.utilisateur.Utilisateur;

public class MouvementFondAttente extends MouvementFond {

    Utilisateur utilisateur;

    public MouvementFondAttente(String i, double montant, String idUtilisateur) throws Exception {
        super(i);
        this.setMontantSansControl(montant);
        this.setUtilisateur(new Utilisateur(idUtilisateur));
    }

    public MouvementFondAttente(String i) {
        super(i);
    }

    public MouvementFondAttente getById(Connection co) throws Exception {
        MouvementFondAttente mvt = null;
        String query = "select * from fondattente where id = (?)";
        try (PreparedStatement statement = co.prepareStatement(query)) {
            statement.setString(1, this.getId());
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    mvt = new MouvementFondAttente(resultSet.getString("id"), resultSet.getDouble("montant"), resultSet.getString("idutilisateur"));
                }
                else {
                    throw new Exception("Fond en attent introuvable avec l\\'identifiant : "+this.getId());
                }
            }
        }
        return mvt;
    }

    public void supprimerDesAttentes(Connection co) throws Exception {
        String query = "DELETE FROM fondattente where id = (?)";
        try (PreparedStatement statement = co.prepareStatement(query)) {
            statement.setString(1, this.getId());
            statement.executeUpdate();
        }
        catch(Exception e) {
            throw e;
        }
    }


    public void insererVersFond(Connection co) throws Exception {
        String query = "INSERT INTO fond (montant, dateMouvement, idUtilisateur) "
        +"SELECT montant, dateMouvement, idUtilisateur FROM fondAttente fA where fA.id = (?) ";
        try (PreparedStatement statement = co.prepareStatement(query)) {
            statement.setString(1, this.getId());
            statement.executeUpdate();
        }
        catch(Exception e) {
            throw e;
        }
    }

    public void transfererVersFond(Connection connection) throws Exception {
        try {
            connection.setAutoCommit(false);
            this.insererVersFond(connection);
            this.supprimerDesAttentes(connection);
            connection.commit();
        }
        catch(Exception e) {
            if(connection!=null) {
                connection.rollback();
            }
            throw e;
        }
        finally {
            if(connection!=null) {
                connection.setAutoCommit(true);
            }
        }
    }

    public MouvementFondAttente() {
        super();
    }

    public MouvementFondAttente(Utilisateur u) {
        this.setUtilisateur(u);
    }

    public MouvementFondAttente [] getRetraitsEnAttentes (Connection connection) throws Exception {
        MouvementFondAttente[] mvmts;
        String query = "select * from v_fondAttenteUtilisateur where montant < 0";

        try (PreparedStatement statement = connection.prepareStatement(query,  ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.last();
                int rowCount = resultSet.getRow();
                resultSet.beforeFirst();

                mvmts = new MouvementFondAttente[rowCount];
                int i=0;
                while(resultSet.next()) {
                    mvmts[i] = new MouvementFondAttente();
                    mvmts[i].setId(resultSet.getString("id"));
                    mvmts[i].setMontant(resultSet.getDouble("montant")*-1);
                    mvmts[i].setDateMouvement(resultSet.getTimestamp("datemouvement").toLocalDateTime());
                    Utilisateur utilisateur = new Utilisateur(resultSet.getString("idutilisateur"), resultSet.getString("nom"), resultSet.getString("prenom"));
                    mvmts[i].setUtilisateur(utilisateur);
                    i++;
                }
            }
            catch(Exception e) {
                throw e;
            }
        }
        catch(Exception er) {
            throw er;
        }
        return mvmts;
    }  

    public MouvementFondAttente [] getDepotsEnAttentes (Connection connection) throws Exception {
        MouvementFondAttente[] mvmts;
        String query = "select * from v_fondAttenteUtilisateur where montant > 0";

        try (PreparedStatement statement = connection.prepareStatement(query,  ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.last();
                int rowCount = resultSet.getRow();
                resultSet.beforeFirst();

                mvmts = new MouvementFondAttente[rowCount];
                int i=0;
                while(resultSet.next()) {
                    mvmts[i] = new MouvementFondAttente();
                    mvmts[i].setId(resultSet.getString("id"));
                    mvmts[i].setMontant(resultSet.getDouble("montant"));
                    mvmts[i].setDateMouvement(resultSet.getTimestamp("datemouvement").toLocalDateTime());
                    Utilisateur utilisateur = new Utilisateur(resultSet.getString("idutilisateur"), resultSet.getString("nom"), resultSet.getString("prenom"));
                    mvmts[i].setUtilisateur(utilisateur);
                    i++;
                }
            }
            catch(Exception e) {
                throw e;
            } 
        }
        catch(Exception err) {
            throw err;
        }
        return mvmts;
    }   

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
    

}
