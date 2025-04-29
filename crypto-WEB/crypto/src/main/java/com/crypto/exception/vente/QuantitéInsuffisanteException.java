package com.crypto.exception.vente;

public class QuantitéInsuffisanteException extends Exception{

    public QuantitéInsuffisanteException(){
        super();
    }

    public QuantitéInsuffisanteException(String message){
        super(message);
    }

    public QuantitéInsuffisanteException(String message, Throwable throwable){
        super(message, throwable);
    }

}
