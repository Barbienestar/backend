package com.itesm.application.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReportDto {
    private Integer id;
    @JsonProperty("medicine_name")
    private String medicineName;
    @JsonProperty("hospital_name")
    private String hospitalName;
    @JsonProperty("status_id")
    private Byte statusId;
    private String description;
    @JsonProperty("created_at")
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