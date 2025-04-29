package com.crypto.exception.authentification;

import com.crypto.model.utilisateur.Utilisateur;

public class AuthentificationException extends Exception{
    
    Utilisateur utilisateur ; 


    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
    
    public AuthentificationException(){
        super();
    }

    public AuthentificationException(String message){
        super(message);
    }

    public AuthentificationException(String message, Throwable throwable){
        super(message, throwable);
    }

    public AuthentificationException(String message, Utilisateur utilisateur){
        super(message);
        setUtilisateur(utilisateur);
    }
}
