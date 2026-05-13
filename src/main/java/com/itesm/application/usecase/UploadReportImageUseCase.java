package com.itesm.application.usecase;

import com.itesm.domain.repository.ImageRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UploadReportImageUseCase {
    private final ImageRepository imageStorageService;

    @Inject
    public UploadReportImageUseCase(ImageRepository imageStorageService) {
        this.imageStorageService = imageStorageService;
    }

    public String execute(byte[] image, String fileName, String contentType) {
        return imageStorageService.uploadImage(image, fileName, contentType);
    }
}
