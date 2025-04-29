package com.crypto.model.firebase;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.crypto.model.crypto.Cryptomonnaie;
import com.crypto.model.utilisateur.Utilisateur;
import com.google.cloud.firestore.FieldValue;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AchatVente {
    
    Utilisateur vendeur ; 
    Utilisateur acheteur ; 
    Cryptomonnaie cryptomonnaie ;
    Timestamp dateTransaction ;
    int quantite ; 

    public void setDateTransaction(LocalDateTime localDateTime) {
        setDateTransaction(Timestamp.valueOf(localDateTime));
    }

    public void setDateTransaction(Timestamp timestamp) {
        this.dateTransaction = (timestamp);
    }

    public AchatVente(Utilisateur vendeur, Utilisateur achateur, Cryptomonnaie cryptomonnaie, LocalDateTime timestamp, int quantite) {
        setAcheteur(achateur);
        setVendeur(vendeur);
        setCryptomonnaie(cryptomonnaie);
        setDateTransaction(timestamp);
        setQuantite(quantite);
    }

    public Map<String, Object> convertirEnMap() {
        
        Map<String, Object> data = new HashMap<>();
        data.put("vendeur", getVendeur().getId());
        data.put("acheteur", getAcheteur().getId());
        data.put("idCrypto", getCryptomonnaie().getId());
        data.put("nomCrypto", getCryptomonnaie().getNom());
        data.put("quantite", getQuantite());
        data.put("dateTransaction", getDateTransaction());
        // data.put("dateTransaction", FieldValue.serverTimestamp());



        return data ; 
       
    }

}
