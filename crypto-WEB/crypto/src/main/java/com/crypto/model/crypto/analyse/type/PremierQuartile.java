package com.crypto.model.crypto.analyse.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.crypto.model.crypto.ChangementCoursCrypto;
import com.crypto.model.crypto.Cryptomonnaie;
import com.crypto.model.crypto.TransactionCrypto;
import com.crypto.model.crypto.analyse.TypeAnalyse;

public class PremierQuartile extends TypeAnalyse {
    
    public PremierQuartile(String id, String libelle) {
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
                double q1 = this.calculerPremierQuartile(listeChangements);
                ChangementCoursCrypto q1Changement = this.creerChangementCoursCrypto(listeChangements.get(0).getCryptomonnaie(), q1);
                resultats.add(q1Changement);
            }
        }

        return resultats.toArray(new ChangementCoursCrypto[0]);
    }

    private double calculerPremierQuartile(List<ChangementCoursCrypto> changements) {
        List<Double> valeurs = changements.stream()
                .map(changement -> changement.getValeur())
                .sorted()
                .collect(Collectors.toList());

        int n = valeurs.size();
        int indexQ1 = (int) Math.floor(0.25 * (n - 1)); // Index basé sur la règle de Tukey

        return valeurs.get(indexQ1);
    }


    private ChangementCoursCrypto creerChangementCoursCrypto(Cryptomonnaie cryptomonnaie, double valeurQ1) {
        ChangementCoursCrypto changement = new ChangementCoursCrypto();
        changement.setCryptomonnaie(cryptomonnaie);
        changement.setValeur(valeurQ1);
        return changement;
    }

}
