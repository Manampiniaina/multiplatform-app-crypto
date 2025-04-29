package com.crypto.model.crypto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.crypto.exception.model.ValeurInvalideException;
import com.crypto.service.util.Util;

public class ChangementCoursCrypto {
    
    String id ; 
    Cryptomonnaie cryptomonnaie ;
    double valeur ; 
    Timestamp date ; 

    // Getter et setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Cryptomonnaie getCryptomonnaie() {
        return this.cryptomonnaie;
    }

    public void setCryptomonnaie(Cryptomonnaie cryptomonnaie) {
        this.cryptomonnaie = cryptomonnaie;
    }

    public void setCryptomonnaie(String idCryptomonnaie) {
        setCryptomonnaie(new Cryptomonnaie(idCryptomonnaie));
    }

    public double getValeur() {
        return this.valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public void setValeur(String valeur) throws ValeurInvalideException {
        try {
            double d = Double.valueOf(valeur) ;
            setValeur(d);
        } catch (Exception e) {
           throw new ValeurInvalideException("Nouvelle valeur de crypto on numérique");
        }
    }

    public Timestamp getDate() {
        return this.date;
    }

    public void setDate() {
        setDate(Util.getDateHeureMaintenant());
    }

    public void setDate(Timestamp date) {
        if (date == null) {
            this.date = Timestamp.valueOf(LocalDateTime.now());
        } else {
            this.date = date;
        }
    }

    public void setDate(String date) throws ValeurInvalideException {
        try {
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            setDate(timestamp);
        } catch (Exception e) {
            setDate(Timestamp.valueOf(LocalDateTime.now()));
        }
    }

    // Constructeur
    public ChangementCoursCrypto() {}

    // public ChangementCoursCrypto(Donnees donnees) {
    //     setValeur(donnees.getValeur());
    //     setDate();
    // }

    public ChangementCoursCrypto(Cryptomonnaie cryptomonnaie, double valeur, Timestamp date) {
        setCryptomonnaie(cryptomonnaie);
        setValeur(valeur);
        setDate(date);
    }

    public ChangementCoursCrypto(String id, Cryptomonnaie cryptomonnaie, double valeur, Timestamp date) {
        setId(id);
        setCryptomonnaie(cryptomonnaie);
        setValeur(valeur);
        setDate(date);
    }

    public ChangementCoursCrypto(String idCryptomonnaie, String valeur, String date) throws ValeurInvalideException{
        setCryptomonnaie(idCryptomonnaie);
        setValeur(valeur);
        setDate(date);
    }

    public ChangementCoursCrypto(Cryptomonnaie cryptomonnaie) throws ValeurInvalideException{
        setCryptomonnaie(cryptomonnaie);
        setValeur(cryptomonnaie.getValeur());
        setDate();
    }

    public void insererHistorique(Connection connection) throws Exception {

        String query = "INSERT INTO historiqueCrypto (id, cours, dateChangement, idCryptomonnaie) VALUES (DEFAULT, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, getValeur());  // Action spécifique, ici "Insertion"
            statement.setTimestamp(2, getDate());  // Action spécifique, ici "Insertion"
            statement.setString(3, getCryptomonnaie().getId());  // Action spécifique, ici "Insertion"
            // Exécuter la requête pour insérer l'historique
            statement.executeUpdate();
        }
    }

    public static ChangementCoursCrypto[] getByCriteria(Connection connection, int secondes) throws Exception {
        List<ChangementCoursCrypto> historiques = new ArrayList<>();

        // Construction de la requête avec des conditions dynamiques
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM v_historique_crypto WHERE 1=1");

        if(secondes==0) {
            Cryptomonnaie[] cryptomonnaies = Cryptomonnaie.getAll(connection);
            for (Cryptomonnaie cryptomonnaie : cryptomonnaies) {
               historiques.add(new ChangementCoursCrypto(cryptomonnaie)); 
            }
            return historiques.toArray(new ChangementCoursCrypto[0]);
        }
        // Ajouter une condition pour les derniers "seconds" si non nul
        if (secondes > 0) {
            queryBuilder.append(" AND dateChangement BETWEEN NOW() - INTERVAL '").append(secondes).append(" seconds'").append("AND NOW() ");
        } 

        String query = queryBuilder.toString();
        // System.out.println("Requete est "+query);

        // Préparer la requête et définir les paramètres dynamiquement
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ChangementCoursCrypto historique = new ChangementCoursCrypto();
                    historique.setId(resultSet.getString("id"));
                    historique.setValeur(resultSet.getDouble("cours"));
                    historique.setDate(resultSet.getTimestamp("dateChangement"));
                    Cryptomonnaie cryptomonnaie = new Cryptomonnaie(resultSet.getString("idcryptomonnaie"), resultSet.getString("nom"), resultSet.getDouble("d_valeur"));
                    historique.setCryptomonnaie(cryptomonnaie);
                    
                    historiques.add(historique);
                }
            }
        }

        return historiques.toArray(new ChangementCoursCrypto[0]); 
    }

    public static ChangementCoursCrypto[] getById(Connection connection, Cryptomonnaie crypto, int secondes) throws Exception {
        List<ChangementCoursCrypto> historiques = new ArrayList<>();

        // Construction de la requête avec des conditions dynamiques
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM historiqueCrypto WHERE 1=1");

        // Ajouter une condition pour les derniers "seconds" si non nul
        if (secondes > 0) {
            queryBuilder.append(" AND dateChangement >= NOW() - INTERVAL '").append(secondes).append(" seconds'");
        } 
        if (crypto!=null) {
            queryBuilder.append(" AND idcryptomonnaie = ?");
        } 

        String query = queryBuilder.toString();

        // Préparer la requête et définir les paramètres dynamiquement
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int parameterIndex = 1;

            if (crypto != null) {
                statement.setString(parameterIndex++, crypto.getId());
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ChangementCoursCrypto historique = new ChangementCoursCrypto();
                    historique.setId(resultSet.getString("id"));
                    historique.setValeur(resultSet.getDouble("cours"));
                    historique.setDate(resultSet.getTimestamp("dateChangement"));
                    historique.setCryptomonnaie(resultSet.getString("idCryptomonnaie"));
                    historiques.add(historique);
                }
            }
        }

        return historiques.toArray(new ChangementCoursCrypto[0]); 
    }

    

    
    @Override
    public String toString() {
        return "Changement crypto {" +
                "id=" + id +
                ", crypto='" + getCryptomonnaie() + '\'' +
                ", valeur=" + getValeur() +
                ", date =" + getDate() +
                '}';
    }
    
}
