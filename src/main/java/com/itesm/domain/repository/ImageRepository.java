package com.itesm.domain.repository;

import java.util.concurrent.TimeUnit;

public interface ImageRepository {
    String uploadImage(byte[] image, String fileName, String contentType);

    String generateSignedUrl(String fileName, long duration, TimeUnit unit);
}
