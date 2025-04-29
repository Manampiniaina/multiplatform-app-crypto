package com.crypto.model.commission.analyse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


import com.crypto.model.crypto.Cryptomonnaie;
import com.crypto.model.crypto.TransactionCrypto;
import com.crypto.model.crypto.analyse.TypeAnalyse;
import com.crypto.model.crypto.analyse.type.Somme;
import com.crypto.model.crypto.analyse.type.Moyenne;

public class Analyseur {
 
    Timestamp dateHeureMin;
    Timestamp dateHeureMax;
    Cryptomonnaie cryptomonnaie;
    int idTypeAnalyse;
    public static final TypeAnalyse [] typesAnalyses = { 
        new Somme("1","Somme"),
        new Moyenne("2","Moyenne")
    }; 

    public Analyseur(String idTypeAnalyse, String dateHeureMin, String dateHeureMax, String idCrypto) throws Exception {
        this.setIdTypeAnalyse(idTypeAnalyse);
        this.setDateHeureMin(dateHeureMin);
        this.setDateHeureMax(dateHeureMax);
        this.setCryptomonnaie(idCrypto);
    }

    public TransactionCrypto [] faireAnalyse(Connection co) throws Exception {
        TransactionCrypto[] transactions = this.getCommissionsCryptosByCriteria(co);
        if(this.getIdTypeAnalyse()!=0) {
            HashMap<String, List<TransactionCrypto>> commissionGroupes = this.groupByIdCrypto(transactions);
            TypeAnalyse typeAnalyse = com.crypto.model.commission.analyse.Analyseur.typesAnalyses[this.getIdTypeAnalyse()-1];
            TransactionCrypto[] answers = typeAnalyse.appliquerTypeAnalyseCommission(commissionGroupes);
            return this.orderByCryptoId(answers);
        }
        else {
            return transactions;
        }
    }

    public TransactionCrypto[] orderByCryptoId(TransactionCrypto[] transactions) {
        Arrays.sort(transactions, Comparator.comparing(c -> c.getCryptomonnaie().getId()));
        return transactions;
    }

    public HashMap<String, List<TransactionCrypto>> groupByIdCrypto(TransactionCrypto[] transactions) {
        HashMap<String, List<TransactionCrypto>> map = new HashMap<>();
        for(TransactionCrypto transaction : transactions) {
            String idCrypto = transaction.getCryptomonnaie().getId();
            if(map.containsKey(idCrypto)) {
                map.get(idCrypto).add(transaction);
            } else {
                List<TransactionCrypto> list = new ArrayList<>();
                list.add(transaction);
                map.put(idCrypto, list);
            }
        }
        return map;
    }

    public TransactionCrypto[] getCommissionsCryptosByCriteria(Connection connection) throws Exception {
        List<TransactionCrypto> transactions = new ArrayList<>();

        String query = "SELECT * FROM v_transaction_crypto WHERE 1=1";
        if(this.getDateHeureMin()!=null) {
            query+=" AND dateTransaction >= (?)";
        }
        if(this.getDateHeureMin()!=null) {
            query+=" AND dateTransaction <= (?)";
        }
        if(this.getCryptomonnaie()!=null) {
            query+=" AND idCryptomonnaie = (?)";
        }
        query+=" ORDER BY idCryptomonnaie, dateTransaction asc";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int parameterIndex = 1;
            if(this.getDateHeureMin()!=null) {
                statement.setTimestamp(parameterIndex, this.getDateHeureMin());
                parameterIndex++;
            }
            if(this.getDateHeureMin()!=null) {
                statement.setTimestamp(parameterIndex, this.getDateHeureMax());
                parameterIndex++;
            }
            if(this.getCryptomonnaie()!=null) {
                statement.setString(parameterIndex, this.getCryptomonnaie().getId());
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    TransactionCrypto transaction = new TransactionCrypto();
                    transaction.setId(resultSet.getString("id"));
                    transaction.setDateTransaction(resultSet.getTimestamp("dateTransaction").toLocalDateTime());
                    transaction.setD_commission(resultSet.getDouble("d_commission"));
                    Cryptomonnaie cryptomonnaie = new Cryptomonnaie(resultSet.getString("idcryptomonnaie"), resultSet.getString("nom"));
                    transaction.setCryptomonnaie(cryptomonnaie);

                    transactions.add(transaction);
                }
            }
        }

        return transactions.toArray(new TransactionCrypto[0]); 
    }


    
    public int getIdTypeAnalyse() {
        return idTypeAnalyse;
    }

    public void setIdTypeAnalyse(String idTypeA) throws Exception {
        if(idTypeA!=null && !idTypeA.isEmpty()) {
            try {
                int id = Integer.parseInt(idTypeA);
                this.setIdTypeAnalyse(id);
            }
            catch(Exception e) {
                throw new Exception("Format de l\\'identifiant du type d`\\'analyse invalide : "+idTypeAnalyse);
            }
        }
    }

    public void setIdTypeAnalyse(int idTypeAnalyse) {
        this.idTypeAnalyse = idTypeAnalyse;
    }

    public Timestamp getDateHeureMin() {
        return dateHeureMin;
    }

    public void setDateHeureMin(String daty) throws Exception {
        if(daty!=null && !daty.isEmpty()) {
            try {
                String formattedDate = daty.replace("T", " ") + ":00";
                Timestamp timestampDaty = Timestamp.valueOf(formattedDate);
                this.setDateHeureMin(timestampDaty);
            } catch (Exception e) {
                throw new Exception("Format invalide pour la date-heure minimale : "+daty);
            }  
        }  
    }

    public void setDateHeureMin(Timestamp dateHeureMin) {
        this.dateHeureMin = dateHeureMin;
    }

    public Timestamp getDateHeureMax() {
        return dateHeureMax;
    }

    public void setDateHeureMax(String daty) throws Exception {
        if(daty!=null && !daty.isEmpty()) {
            try {
                String formattedDate = daty.replace("T", " ") + ":00";
                Timestamp timestampDaty = Timestamp.valueOf(formattedDate);
                this.setDateHeureMax(timestampDaty);
            } catch (Exception e) {
                throw new Exception("Format invalide pour la date-heure maximale : "+daty);
            }   
        } 
    }
    
    public void setDateHeureMax(Timestamp dateHeureMax) {
        this.dateHeureMax = dateHeureMax;
    }

    public void setCryptomonnaie(String idCryptomonnaie) {
        if(idCryptomonnaie!=null && !idCryptomonnaie.isEmpty()) {
            if(!idCryptomonnaie.equals("all")) {
                this.setCryptomonnaie(new Cryptomonnaie(idCryptomonnaie));
            }
        }
    }

    public Cryptomonnaie getCryptomonnaie() {
        return cryptomonnaie;
    }

    public void setCryptomonnaie(Cryptomonnaie cryptomonnaie) {
        this.cryptomonnaie = cryptomonnaie;
    }
    
    
}
