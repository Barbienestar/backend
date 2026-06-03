package com.itesm.domain.exceptions;

public class FirebaseUserCreationException extends RuntimeException {
    public FirebaseUserCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
