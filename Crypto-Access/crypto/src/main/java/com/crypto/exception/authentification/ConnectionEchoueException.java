package com.crypto.exception.authentification;

import com.crypto.model.utilisateur.Utilisateur;

public class ConnectionEchoueException extends AuthentificationException{
    
      public ConnectionEchoueException(){
        super();
    }

    public ConnectionEchoueException(String message){
        super(message);
    }

    public ConnectionEchoueException(String message, Throwable throwable){
        super(message, throwable);
    }

    public ConnectionEchoueException(String message, Utilisateur utilisateur){
        super(message);
        setUtilisateur(utilisateur);
    }
}
