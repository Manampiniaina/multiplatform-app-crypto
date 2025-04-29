package com.crypto.model.crypto.analyse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.crypto.model.crypto.ChangementCoursCrypto;
import com.crypto.model.crypto.Cryptomonnaie;
import com.crypto.model.crypto.analyse.type.EcartType;
import com.crypto.model.crypto.analyse.type.Max;
import com.crypto.model.crypto.analyse.type.Min;
import com.crypto.model.crypto.analyse.type.Moyenne;
import com.crypto.model.crypto.analyse.type.PremierQuartile;

public class Analyseur {
    
    Timestamp dateHeureMin;
    Timestamp dateHeureMax;
    List<Cryptomonnaie> cryptomonnaies;

    int idTypeAnalyse;
    public static final TypeAnalyse [] typesAnalyses = { 
        new PremierQuartile("1","1er quartile"),
        new Max("2","Max"),
        new Min("3","Min"),
        new Moyenne("4","Moyenne"),
        new EcartType("5","Ecart-type"),
    }; 

        
    public ChangementCoursCrypto[] orderByCryptoId(ChangementCoursCrypto[] changements) {
        Arrays.sort(changements, Comparator.comparing(c -> c.getCryptomonnaie().getId()));
        return changements;
    }

    public HashMap<String, List<ChangementCoursCrypto>> groupByIdCrypto(ChangementCoursCrypto[] changements) {
        HashMap<String, List<ChangementCoursCrypto>> map = new HashMap<>();
        for(ChangementCoursCrypto changement : changements) {
            String idCrypto = changement.getCryptomonnaie().getId();
            if(map.containsKey(idCrypto)) {
                map.get(idCrypto).add(changement);
            } else {
                List<ChangementCoursCrypto> list = new ArrayList<>();
                list.add(changement);
                map.put(idCrypto, list);
            }
        }
        return map;
    }

    public ChangementCoursCrypto [] faireAnalyse(Connection co) throws Exception {
        ChangementCoursCrypto [] changements = this.getCryptosByCriteria(co);
        if(this.getIdTypeAnalyse()!=0) {
            HashMap<String, List<ChangementCoursCrypto>> changementsGroupes = this.groupByIdCrypto(changements);
            ChangementCoursCrypto[] nouveauxChgts = Analyseur.typesAnalyses[this.getIdTypeAnalyse()-1].appliquerTypeAnalyse(changementsGroupes);
            return this.orderByCryptoId(nouveauxChgts);
        }
        else {
            return changements;
        }
    }

    public Analyseur() {
    }

    public Analyseur(String idTypeAnalyse, String dateHeureMin, String dateHeureMax, String [] idCryptos) throws Exception {
        this.setIdTypeAnalyse(idTypeAnalyse);
        this.setDateHeureMin(dateHeureMin);
        this.setDateHeureMax(dateHeureMax);
        this.setCryptomonnaies(idCryptos);
    }

    private String generateCryptosQuery() {
        String query = " AND (";
        List<Cryptomonnaie> cryptomonnaies = this.getCryptomonnaies();
        for(Cryptomonnaie cryptomonnaie : cryptomonnaies) {
            query+="idCryptomonnaie = (?)";
            if(cryptomonnaie!=cryptomonnaies.get(cryptomonnaies.size()-1)) {
                query+=" OR ";
            }
        }
        query+=")";
        return query;
    }

    private void setCryptomonnaiesToStmt(PreparedStatement statement, int parameterIndex) throws Exception {
        List<Cryptomonnaie> cryptomonnaies = this.getCryptomonnaies();
        for(Cryptomonnaie cryptomonnaie : cryptomonnaies) {
            statement.setString(parameterIndex, cryptomonnaie.getId());
            parameterIndex++;
        }
    }

    public ChangementCoursCrypto[] getCryptosByCriteria(Connection connection) throws Exception {
        List<ChangementCoursCrypto> historiques = new ArrayList<>();

        String query = "SELECT * FROM v_historique_crypto WHERE 1=1";
        if(this.getDateHeureMin()!=null) {
            query+=" AND dateChangement >= (?)";
        }
        if(this.getDateHeureMin()!=null) {
            query+=" AND dateChangement <= (?)";
        }
        if(this.getCryptomonnaies()!=null && !this.getCryptomonnaies().isEmpty()) {
            query+=this.generateCryptosQuery();
        }

        query+=" ORDER BY idCryptomonnaie, dateChangement asc";

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
            if(this.getCryptomonnaies()!=null && !this.getCryptomonnaies().isEmpty()) {
                this.setCryptomonnaiesToStmt(statement, parameterIndex);
            }

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

    public void setCryptomonnaies(String [] idCryptomonnaies) {
        this.cryptomonnaies = new ArrayList<>();
        if(idCryptomonnaies!=null && idCryptomonnaies.length>0) {
            if(!idCryptomonnaies[0].equals("all")) {
                for(String id : idCryptomonnaies) {
                    Cryptomonnaie cryptomonnaie = new Cryptomonnaie(id);
                    this.cryptomonnaies.add(cryptomonnaie);
                }
            }
        }
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
    
    public List<Cryptomonnaie> getCryptomonnaies() {
        return cryptomonnaies;
    }
    
    public void setCryptomonnaies(List<Cryptomonnaie> cryptomonnaies) {
        this.cryptomonnaies = cryptomonnaies;
    }

}
