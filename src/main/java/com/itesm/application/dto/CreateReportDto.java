package com.itesm.application.dto;

public class CreateReportDto {
    private Integer medicineId;
    private Integer hospitalId;
    private String description;

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