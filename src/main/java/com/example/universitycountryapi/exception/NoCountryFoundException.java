package com.example.universitycountryapi.exception;

public class NoCountryFoundException extends RuntimeException {
    public NoCountryFoundException(String message){
        super(message);
    }
}
