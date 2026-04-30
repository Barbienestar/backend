package com.itesm.domain.repository;

public interface ImageStorageService {
    String uploadImage(byte[] image, String fileName, String contentType);
}
