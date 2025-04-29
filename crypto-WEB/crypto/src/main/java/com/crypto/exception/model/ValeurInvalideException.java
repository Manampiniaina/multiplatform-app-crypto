package com.crypto.exception.model;

public class ValeurInvalideException extends Exception {
    
    public ValeurInvalideException(){
        super();
    }

    public ValeurInvalideException(String message){
        super(message);
    }

    public ValeurInvalideException(String message, Throwable throwable){
        super(message, throwable);
    }
}
