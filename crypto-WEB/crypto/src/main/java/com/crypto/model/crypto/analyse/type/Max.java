package com.crypto.model.crypto.analyse.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crypto.model.crypto.ChangementCoursCrypto;
import com.crypto.model.crypto.TransactionCrypto;
import com.crypto.model.crypto.analyse.TypeAnalyse;

public class Max extends TypeAnalyse {
    
    public Max(String id, String libelle) {
        super(id, libelle);
    }

    public TransactionCrypto[] appliquerTypeAnalyseCommission(HashMap<String, List<TransactionCrypto>> transactions){
        return new TransactionCrypto[0];
    }

    public ChangementCoursCrypto[] appliquerTypeAnalyse(HashMap<String, List<ChangementCoursCrypto>> changements) {
        List<ChangementCoursCrypto> resultats = new ArrayList<>();

        for (Map.Entry<String, List<ChangementCoursCrypto>> entry : changements.entrySet()) {
            List<ChangementCoursCrypto> listeChangements = entry.getValue();

            if (listeChangements != null && !listeChangements.isEmpty()) {
                ChangementCoursCrypto maxChangement = Collections.max(listeChangements, Comparator.comparingDouble(c -> c.getValeur()));
                resultats.add(maxChangement);
            }
        }

        return resultats.toArray(new ChangementCoursCrypto[0]);
    }

}
