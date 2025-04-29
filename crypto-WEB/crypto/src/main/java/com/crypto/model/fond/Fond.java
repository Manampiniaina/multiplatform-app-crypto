package com.crypto.model.fond;

import com.crypto.model.utilisateur.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Fond {
    
    double montant;
    List<MouvementFond> depot;
    List<MouvementFond> retrait;

    public Fond() {
        setDepot(new ArrayList<>());
        setRetrait(new ArrayList<>());
    }

    public void ajouterMontant(double mnt) {
        setMontant(getMontant()+mnt);
    }

    public void ajouterMouvement(MouvementFond mouvementFond) {
        if(mouvementFond.estDepot()) {
            this.depot.add(mouvementFond);
        } else {
            this.retrait.add(mouvementFond);
        }
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public List<MouvementFond> getDepot() {
        return depot;
    }

    public void setDepot(List<MouvementFond> depot) {
        this.depot = depot;
    }

    public List<MouvementFond> getRetrait() {
        return retrait;
    }

    public void setRetrait(List<MouvementFond> retrait) {
        this.retrait = retrait;
    }

    public static Fond getFondByUtilisateur(Utilisateur u, Connection connection) throws Exception {
        
        Fond fond = new Fond() ;
        String query = "SELECT * FROM fond where idUtilisateur = (?)";

        try(PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, u.getId());

            try (ResultSet rs = statement.executeQuery()) { 
                while (rs.next()) {
                    MouvementFond mvt=new MouvementFond(rs.getString("id"), 0, rs.getTimestamp("dateMouvement").toLocalDateTime(), null );
                    mvt.setMontantSansControl(rs.getDouble("montant"));
                    fond.ajouterMouvement(mvt);
                    fond.ajouterMontant(mvt.getMontant());
                }
            }
        }
    
        return fond;
    }


}
