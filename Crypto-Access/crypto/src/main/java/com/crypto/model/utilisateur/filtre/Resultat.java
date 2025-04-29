package com.crypto.model.utilisateur.filtre;

import com.crypto.model.portefeuille.PorteFeuilleDetails;
import com.crypto.model.transaction.Transaction;
import com.crypto.model.utilisateur.Utilisateur;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Resultat {
    Utilisateur u;
    double totalAchat;
    double totalVente;
    List<PorteFeuilleDetails> ptd;

    public Resultat() {
    }

    public Utilisateur getU() {
        return u;
    }

    public void setU(Utilisateur u) {
        this.u = u;
    }

    public double getTotalAchat() {
        return totalAchat;
    }

    public void setTotalAchat(double totalAchat) {
        this.totalAchat = totalAchat;
    }

    public double getTotalVente() {
        return totalVente;
    }

    public void setTotalVente(double totalVente) {
        this.totalVente = totalVente;
    }

    public List<PorteFeuilleDetails> getPtd() {
        return ptd;
    }

    public void setPtd(List<PorteFeuilleDetails> ptd) {
        this.ptd = ptd;
    }

    public static List<Resultat> getResultatByDate(LocalDateTime dateMax, Connection c) throws Exception{
        List<Resultat> resultats=new ArrayList<>();
        List<Utilisateur> listeU=Utilisateur.getAll(c);
        // System.out.println("utilisateur"+listeU.size());
        if(dateMax==null) {
            dateMax = LocalDateTime.now();
        }
        for (Utilisateur u:listeU) {
            Resultat res=new Resultat();
            u.setTransaction(c,dateMax);
            u.setPorteFeuilleByConnection(c);
            res.setU(u);
            res.setTotalAchat(Transaction.calculateTotal(u.getTransaction().getAchat()));
            res.setTotalVente(Transaction.calculateTotal(u.getTransaction().getVente()));
            res.setPtd(u.getPorteFeuille().getPorteFeuilleDetails());
            resultats.add(res);
        }
        return  resultats;
    };
}
