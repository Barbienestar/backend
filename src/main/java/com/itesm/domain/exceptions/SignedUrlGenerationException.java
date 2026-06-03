package com.itesm.domain.exceptions;

public class SignedUrlGenerationException extends RuntimeException {
    public SignedUrlGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
