package com.crypto.model.crypto.analyse.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crypto.model.crypto.ChangementCoursCrypto;
import com.crypto.model.crypto.TransactionCrypto;
import com.crypto.model.crypto.analyse.TypeAnalyse;

public class Somme extends TypeAnalyse {

    public Somme(String id, String libelle) {
        super(id, libelle);
    }
    
    public TransactionCrypto getSomme(List<TransactionCrypto> transactions) {
        if(transactions!=null && !transactions.isEmpty()) {
            double somme = 0;
            for(TransactionCrypto t : transactions) {
                somme += t.getD_commission();
            }
            TransactionCrypto sommeTransaction = new TransactionCrypto();
            sommeTransaction.setCryptomonnaie(transactions.get(0).getCryptomonnaie());
            sommeTransaction.setD_commission(somme);
            // System.out.println("\n Tsy null ka");
            return sommeTransaction;
        }
        return null;
    }

    public TransactionCrypto[] appliquerTypeAnalyseCommission(HashMap<String, List<TransactionCrypto>> transactions){
        // System.out.println("are we even here ?");
        List<TransactionCrypto> resultats = new ArrayList<>();
        for (Map.Entry<String, List<TransactionCrypto>> entry : transactions.entrySet()) {
            List<TransactionCrypto> listeChangements = entry.getValue();
            resultats.add(this.getSomme(listeChangements));
        }    
        return resultats.toArray(new TransactionCrypto[resultats.size()]);
    }


    public ChangementCoursCrypto[] appliquerTypeAnalyse( HashMap<String, List<ChangementCoursCrypto>> changements) {
        return new ChangementCoursCrypto[0];
    }


}
