package com.itesm.application.usecase;

import com.itesm.domain.repository.ImageStorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UploadReportImageUseCase {
    private final ImageStorageService imageStorageService;

    @Inject
    public UploadReportImageUseCase(ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
    }

    public String execute(byte[] image, String fileName, String contentType) {
        return imageStorageService.uploadImage(image, fileName, contentType);
    }
}
