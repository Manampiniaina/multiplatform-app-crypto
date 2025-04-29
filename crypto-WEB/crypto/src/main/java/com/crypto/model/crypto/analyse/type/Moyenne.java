package com.crypto.model.crypto.analyse.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crypto.model.crypto.ChangementCoursCrypto;
import com.crypto.model.crypto.TransactionCrypto;
import com.crypto.model.crypto.analyse.TypeAnalyse;

public class Moyenne extends TypeAnalyse {
    
    public Moyenne(String id, String libelle) {
        super(id, libelle);
    }

    public TransactionCrypto getMoyennantCommission(List<TransactionCrypto> transactions) {
        if(transactions!=null && !transactions.isEmpty()) {
            double somme = 0;
            for(TransactionCrypto t : transactions) {
                somme += t.getD_commission();
            }
            double moyenne = somme/transactions.size();
            TransactionCrypto sommeTransaction = new TransactionCrypto();
            sommeTransaction.setCryptomonnaie(transactions.get(0).getCryptomonnaie());
            sommeTransaction.setD_commission(moyenne);
            return sommeTransaction;
        }
        return null;
    }

    public TransactionCrypto[] appliquerTypeAnalyseCommission(HashMap<String, List<TransactionCrypto>> transactions){
        // System.out.println("are we even here ?");
        List<TransactionCrypto> resultats = new ArrayList<>();
        for (Map.Entry<String, List<TransactionCrypto>> entry : transactions.entrySet()) {
            List<TransactionCrypto> listeChangements = entry.getValue();
            resultats.add(this.getMoyennantCommission(listeChangements));
        }    
        return resultats.toArray(new TransactionCrypto[resultats.size()]);
    }

    public ChangementCoursCrypto getMoyennant(List<ChangementCoursCrypto> lsChangements) {
        if (lsChangements != null && !lsChangements.isEmpty()) {
            double somme = 0;
            for (ChangementCoursCrypto changement : lsChangements) {
                somme += changement.getValeur();
            }
            double moyenne = somme / lsChangements.size();

            ChangementCoursCrypto moyenneChangement = new ChangementCoursCrypto();
            moyenneChangement.setCryptomonnaie(lsChangements.get(0).getCryptomonnaie()); 
            moyenneChangement.setValeur(moyenne);

            return moyenneChangement;
        }
        return null;
    }

    public ChangementCoursCrypto[] appliquerTypeAnalyse(HashMap<String, List<ChangementCoursCrypto>> changements) {
        List<ChangementCoursCrypto> resultats = new ArrayList<>();

        for (Map.Entry<String, List<ChangementCoursCrypto>> entry : changements.entrySet()) {
            List<ChangementCoursCrypto> listeChangements = entry.getValue();
            resultats.add(this.getMoyennant(listeChangements));
        }

        return resultats.toArray(new ChangementCoursCrypto[0]);
    }


}
