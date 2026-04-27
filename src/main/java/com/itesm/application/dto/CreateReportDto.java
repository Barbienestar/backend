package com.itesm.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateReportDto {
    @NotNull(message = "Description is required")
    private Integer medicineId;

    @NotNull(message = "Description is required")
    private Integer hospitalId;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public CreateReportDto() {}

    public Integer getMedicineId() {
        return medicineId;
    }

    public Integer getHospitalId() {
        return hospitalId;
    }

    public String getDescription() {
        return description;
    }

    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }

    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
