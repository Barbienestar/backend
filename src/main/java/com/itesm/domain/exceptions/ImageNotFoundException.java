package com.itesm.domain.exceptions;

public class ImageNotFoundException extends RuntimeException {
    public ImageNotFoundException(String fileName) {
        super("Image not found: " + fileName);
    }
}
