package com.example.universitycountryapi.Exception;

public class NoCountryFoundException extends RuntimeException {
    public NoCountryFoundException(String message){
        super(message);
    }
}
