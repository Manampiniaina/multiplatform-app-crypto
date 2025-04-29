package com.crypto.model.crypto.analyse.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crypto.model.crypto.ChangementCoursCrypto;
import com.crypto.model.crypto.TransactionCrypto;
import com.crypto.model.crypto.analyse.TypeAnalyse;

public class EcartType extends TypeAnalyse  {
 
    public EcartType(String id, String libelle) {
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
                double moyenne = calculerMoyenne(listeChangements);
                double variance = calculerVariance(listeChangements, moyenne);
                double ecartType = Math.sqrt(variance); 
                ChangementCoursCrypto ecartTypeChangement = new ChangementCoursCrypto();
                ecartTypeChangement.setCryptomonnaie(listeChangements.get(0).getCryptomonnaie());
                ecartTypeChangement.setValeur(ecartType);
                resultats.add(ecartTypeChangement);
            }
        }
        return resultats.toArray(new ChangementCoursCrypto[0]);
    }

    private double calculerMoyenne(List<ChangementCoursCrypto> changements) {
        double somme = 0;
        for (ChangementCoursCrypto changement : changements) {
            somme += changement.getValeur();
        }
        return somme / changements.size();
    }

    private double calculerVariance(List<ChangementCoursCrypto> changements, double moyenne) {
        double variance = 0;
        for (ChangementCoursCrypto changement : changements) {
            variance += Math.pow(changement.getValeur() - moyenne, 2);
        }
        return variance / changements.size();
    }
    
}
