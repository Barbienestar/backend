package com.itesm.infrastructure.storage;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.itesm.domain.repository.ImageStorageService;
import jakarta.enterprise.context.ApplicationScoped;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class FirebaseStorageService implements ImageStorageService {
    @Override
    public String uploadImage(byte[] image, String fileName, String contentType) {
        try {
            Bucket bucket = StorageClient.getInstance().bucket();
            Blob blob = bucket.create(fileName, image, contentType);
            URL signedUrl = blob.signUrl(7, TimeUnit.DAYS);
            return signedUrl.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image: " + e.getMessage(), e);
        }
    }
}
