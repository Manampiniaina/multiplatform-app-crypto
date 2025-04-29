package com.crypto.model.commission;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.crypto.model.crypto.Cryptomonnaie;

public class Commission {

    String id;
    double pourcentage;
    LocalDateTime dateChangement;
    Cryptomonnaie cryptomonnaie;

    public Commission() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPourcentage() {
        return pourcentage;
    }
    public double getPourcentageSansPourcentage() {
        return pourcentage/100;
    }

    public void setPourcentage(double pourcentage) {
        this.pourcentage = pourcentage;
    }

    public LocalDateTime getDateChangement() {
        return dateChangement;
    }

    public void setDateChangement(LocalDateTime dateChangement) {
        this.dateChangement = dateChangement;
    }

    public Cryptomonnaie getCryptomonnaie() {
        return cryptomonnaie;
    }

    public void setCryptomonnaie(Cryptomonnaie cryptomonnaie) {
        if(!cryptomonnaie.getId().equals("tous")) {
            this.cryptomonnaie = cryptomonnaie;
            this.cryptomonnaie.setCommission(this);
        }
    }

    public static Commission getByIdCryptoAndDate(Connection connection,Cryptomonnaie crypto,LocalDateTime date) throws SQLException {
        String query = "SELECT * FROM commission WHERE idCryptomonnaie = ? and dateChangement<=? order by dateChangement desc limit 1 ";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1,crypto.getId() );
            statement.setTimestamp(2, Timestamp.valueOf(date));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Commission commission = new Commission();
                    commission.setId(resultSet.getString("id"));
                    commission.setPourcentage(resultSet.getDouble("pourcentage"));
                    commission.setDateChangement(resultSet.getTimestamp("dateChangement").toLocalDateTime());
                    return commission;
                }
            }
        }

        return null;
    }

    public void mettreAjourCommissionCrypto(Connection co) throws Exception {
        try {
            co.setAutoCommit(false);
            this.getCryptomonnaie().mettreAjour(co);
            this.insert(co, this.getCryptomonnaie());
            co.commit();
        } catch (Exception e) {
            if (co != null) {
                co.rollback();
            }
            throw e;
        } finally {
            if (co != null) {
                co.setAutoCommit(true);
            }
        }
    }

    public void insertBatchCommission(Connection co, Cryptomonnaie[] cryptos) throws SQLException {
        for(Cryptomonnaie crypto : cryptos) {
            this.insert(co, crypto);
        }
    }

    public void miseAjourTousCryptos(Connection co) throws Exception {
        PreparedStatement st = null;
        String query = "update cryptomonnaie set d_commission=(?)";
        try {
            st = co.prepareStatement(query);
            st.setDouble(1, this.getPourcentage());
            st.executeUpdate();
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public void mettreAjourCommissionTousCryptos(Connection co) throws Exception {
        try {
            co.setAutoCommit(false);
            this.miseAjourTousCryptos(co);
            this.insertBatchCommission(co, Cryptomonnaie.getAll(co));
            co.commit();
        } catch (Exception e) {
            if (co != null) {
                co.rollback();
            }
            throw e;
        } finally {
            if (co != null) {
                co.setAutoCommit(true);
            }
        }
    }

    public void modifierCommission(Connection co) throws Exception {
        if(this.getCryptomonnaie()==null) {
            this.mettreAjourCommissionTousCryptos(co);
        }
        else {
            this.mettreAjourCommissionCrypto(co);
        }
    }
    
    public void insert(Connection connection, Cryptomonnaie crypto) throws SQLException {
        String query = "INSERT INTO commission (idCryptomonnaie, pourcentage) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, crypto.getId());
            statement.setDouble(2, this.getPourcentage());
            statement.executeUpdate();
        }
    }
    
    public static List<Commission> findByCryptoAndDateBetween(Connection connection, Cryptomonnaie crypto, LocalDateTime dateMin, LocalDateTime dateMax) throws SQLException {
        String query = "SELECT * FROM commission WHERE dateChangement BETWEEN ? AND ?";
        if (crypto != null) {
            query += " AND idCryptomonnaie = ?";
        }
        query += " ORDER BY dateChangement ASC";

        List<Commission> commissions = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, Timestamp.valueOf(dateMin));
            statement.setTimestamp(2, Timestamp.valueOf(dateMax));

            if (crypto != null) {
                statement.setString(3, crypto.getId());
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Commission commission = new Commission();
                    commission.setId(resultSet.getString("id"));
                    commission.setPourcentage(resultSet.getDouble("pourcentage"));
                    commission.setDateChangement(resultSet.getTimestamp("dateChangement").toLocalDateTime());
                    commissions.add(commission);
                }
            }
        }
        return commissions;
    }

    public static double calculateTotal(List<Commission> commissions) {
        double total = 0;
        for (Commission commission : commissions) {
            total += commission.getPourcentage();
        }
        return total;
    }

    public static double calculateAverage(List<Commission> commissions) {
        if (commissions.isEmpty()) {
            return 0;
        }
        double total = calculateTotal(commissions);
        return total / commissions.size();
    }

}

