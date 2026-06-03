package com.itesm.domain.exceptions;

public class FirebaseUserDeletionException extends RuntimeException {
    public FirebaseUserDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
