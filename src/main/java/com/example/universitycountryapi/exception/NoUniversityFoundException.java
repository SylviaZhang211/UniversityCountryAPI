package com.example.universitycountryapi.exception;



public class NoUniversityFoundException extends RuntimeException {
    public NoUniversityFoundException(String message) {
        super(message);
    }
}

