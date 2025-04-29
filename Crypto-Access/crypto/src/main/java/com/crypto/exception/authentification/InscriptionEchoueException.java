package com.crypto.exception.authentification;

import com.crypto.model.utilisateur.Utilisateur;

public class InscriptionEchoueException extends AuthentificationException{

    public InscriptionEchoueException(){
        super();
    }

    public InscriptionEchoueException(String message){
        super(message);
    }

    public InscriptionEchoueException(String message, Throwable throwable){
        super(message, throwable);
    }

    public InscriptionEchoueException(String message, Utilisateur utilisateur){
        super(message);
        setUtilisateur(utilisateur);
    }
}
