package com.itesm.infrastructure.persistence.repository;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.itesm.domain.repository.ImageRepository;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class FirebaseStorageService implements ImageRepository {
    @Override
    public String uploadImage(byte[] image, String fileName, String contentType) {
        try {
            Bucket bucket = StorageClient.getInstance().bucket();
            bucket.create(fileName, image, contentType);
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image: " + e.getMessage(), e);
        }
    }

    @Override
    public String generateSignedUrl(String fileName, long duration, TimeUnit unit) {
        try {
            Bucket bucket = StorageClient.getInstance().bucket();
            Blob blob = bucket.get(fileName);
            if (blob == null) {
                throw new RuntimeException("Image not found: " + fileName);
            }
            return blob.signUrl(duration, unit).toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate signed URL: " + e.getMessage(), e);
        }
    }
}
