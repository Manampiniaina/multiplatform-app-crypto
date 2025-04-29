package com.crypto.model.firebase;

import java.sql.Connection;
import java.sql.Timestamp;

import com.crypto.model.crypto.Cryptomonnaie;
import com.crypto.model.utilisateur.Utilisateur;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
@NoArgsConstructor
public class HistoriqueCryptoFavori {
    
    Cryptomonnaie cryptomonnaie ; 
    Utilisateur utilisateur ;
    String operation;
    Timestamp dateChangement;

    public void setCryptomonnaie(String idCrypto) {
        cryptomonnaie = (new Cryptomonnaie(idCrypto));
    }

    public void setUtilisateur(String idUtilisateur) {
        utilisateur = (new Utilisateur(idUtilisateur));
    }

    public HistoriqueCryptoFavori(String idCrypto, String idUtilisateur, String opearatioString, Timestamp date) {
        setCryptomonnaie(idCrypto);
        setUtilisateur(idUtilisateur);
        setOperation(opearatioString);
        setDateChangement(date);
    }
    

    public static void modifier(Connection connection, HistoriqueCryptoFavori[] historiqueCryptoFavoris) throws Exception {

        try {
            for (HistoriqueCryptoFavori historiqueCryptoFavori : historiqueCryptoFavoris) {
                System.out.println(historiqueCryptoFavori.toString());
                if(historiqueCryptoFavori.getOperation().equalsIgnoreCase("INSERT")) {
                    historiqueCryptoFavori.getUtilisateur().ajouterFavori(connection, historiqueCryptoFavori.getCryptomonnaie());  
                } else historiqueCryptoFavori.getUtilisateur().supprimerFavori(connection, historiqueCryptoFavori.getCryptomonnaie());  
    
            }
        } catch (Exception e) {
            throw e ; 
        }
        

    }

    @Override 
    public String toString() {

        return " Cryptomonnaie = "+getCryptomonnaie() +
               ", Utilisateur = "+getUtilisateur() +
               ", date changement = "+getDateChangement()+
               ", opération = " +getOperation();
    }
}
