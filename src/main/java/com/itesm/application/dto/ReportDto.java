package com.itesm.application.dto;

import java.time.LocalDateTime;

public class ReportDto {
    private Integer id;
    private String medicineName;
    private String hospitalName;
    private Byte statusId;
    private String description;
    private LocalDateTime createdAt;

    public ReportDto() {}

    public ReportDto(Integer id, String medicineName, String hospitalName, Byte statusId, String description, LocalDateTime createdAt) {
        this.id = id;
        this.medicineName = medicineName;
        this.hospitalName = hospitalName;
        this.statusId = statusId;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getHospitalName() {
        return hospitalName;
    }



    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public void setStatusId(Byte statusId) {
        this.statusId = statusId;
    }

    public Byte getStatusId() {

        return statusId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}