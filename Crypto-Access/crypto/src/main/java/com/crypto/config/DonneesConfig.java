package com.crypto.config;

import java.sql.Connection ; 
import java.sql.ResultSet ;

import org.springframework.stereotype.Service;

import java.sql.PreparedStatement ; 

@Service
public class DonneesConfig {

    public static final double VARIATION_CRYPTO = 5 ;

    // Durées des dernières minutes 

    public static final int SECONDES_CONSIDEREE = 180;
    public static final int FREQUENCE_CHANGEMENT = 60000;
    public static final int FREQUENCE_FIREBASE = 60000;

    
    static final String frequenceCrypto = "frequence crypto" ;
    static final String frequenceFirebase = "frequence firebase" ;
    static final String secondeConsideree = "secondes considere" ;
    static final String variationCrypto = "variation crypto" ;

    // public static final String tempIdUtilisateur = "USR000000001";
    
    public double getFrequenceCrypto(Connection connection) throws Exception {
        String sql = "SELECT * from configuration where id = ? " ; 

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, frequenceCrypto) ;
            try(ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    return rs.getDouble("valeur") ; 
                }
            }
        }

        return 0 ; 
    }

    public long getFrequenceFirebase(Connection connection) throws Exception {
        String sql = "SELECT * from configuration where id = ? " ; 

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, frequenceFirebase) ;
            try(ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    return rs.getLong("valeur") ; 
                }
            }
        }

        return 0 ; 
    }

    public int getSecondeConsideree(Connection connection) throws Exception {
        String sql = "SELECT * from configuration where id = ? " ; 

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, secondeConsideree) ;
            try(ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    return rs.getInt("valeur") ; 
                }
            }
        }

        return 0 ; 
    }

    public double getVariationCrypto(Connection connection) throws Exception {
        String sql = "SELECT * from configuration where id = ? " ; 

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, variationCrypto) ;
            try(ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    return rs.getDouble("valeur") ; 
                }
            }
        }

        return 0 ; 
    }

}
