package com.itesm.domain.models;

import java.time.LocalDateTime;

public class Medicine {
    private Integer id;
    private String genericName;
    private String dosageForm;
    private String strength;
    private String presentation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Medicine() {}

    public Medicine(Integer id, String genericName, String dosageForm,
                    String strength, String presentation,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.genericName = genericName;
        this.dosageForm = dosageForm;
        this.strength = strength;
        this.presentation = presentation;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getGenericName() { return genericName; }
    public void setGenericName(String genericName) { this.genericName = genericName; }

    public String getDosageForm() { return dosageForm; }
    public void setDosageForm(String dosageForm) { this.dosageForm = dosageForm; }

    public String getStrength() { return strength; }
    public void setStrength(String strength) { this.strength = strength; }

    public String getPresentation() { return presentation; }
    public void setPresentation(String presentation) { this.presentation = presentation; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}