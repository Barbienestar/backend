package com.itesm.domain.repository;

/**
 * ImageStorageService
 */
public interface ImageStorageService {
    String uploadImage(byte[] image, String fileName, String contentType);
}
