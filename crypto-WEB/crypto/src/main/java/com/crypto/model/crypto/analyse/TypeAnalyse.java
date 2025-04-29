package com.crypto.model.crypto.analyse;

import java.util.HashMap;
import java.util.List;

import com.crypto.model.crypto.ChangementCoursCrypto;
import com.crypto.model.crypto.TransactionCrypto;

public abstract class TypeAnalyse {
    
    String id;
    String libelle;

    public abstract TransactionCrypto[] appliquerTypeAnalyseCommission(HashMap<String, List<TransactionCrypto>> transactions);

    public abstract ChangementCoursCrypto[] appliquerTypeAnalyse( HashMap<String, List<ChangementCoursCrypto>> changements);

    public TypeAnalyse(String id, String libelle) {
        this.setId(id);
        this.setLibelle(libelle);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

}
