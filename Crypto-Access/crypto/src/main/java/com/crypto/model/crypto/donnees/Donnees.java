package com.crypto.model.crypto.donnees;

import java.util.Random;

import com.crypto.config.DonneesConfig;

public class Donnees {

    double valeur; 
    double borne;  

     // Getter pour valeur
    public double getValeur() {
        return valeur;
    }

    // Setter pour valeur
    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    // Getter pour borne
    public double getBorne() {
        return borne;
    }

    // Setter pour borne
    public void setBorne(double borne) {
        this.borne = borne;
    }

    // Constructeur
    public Donnees(double valeur, double borne) {
        setValeur(valeur);
        setBorne(borne);
    }

    public Donnees(double valeur) {
        setValeur(valeur);
        setBorne(DonneesConfig.VARIATION_CRYPTO);
    }

    public Donnees() {
        setBorne(DonneesConfig.VARIATION_CRYPTO);
    }

    // Méthode pour générer une valeur aléatoire
    public double genererValeurAleatoire() {

        double variation = (getBorne() / 100) * getValeur(); 
        Random random = new Random();
        double offset = (2 * random.nextDouble() - 1) * variation; // Offset aléatoire entre -variation et +variation
        return valeur + offset;
    }

    @Override 
    public String toString(){
        return "{ valeur : "+ getValeur()+
                " borne : "+ getBorne()+
                "}";
    }
   
}
