package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MedicineDto {
    private Integer id;
    @JsonProperty("generic_name")
    private String genericName;
    @JsonProperty("dosage_form")
    private String dosageForm;
    private String strength;
    private String presentation;

    public MedicineDto() {}

    public MedicineDto(Integer id, String genericName, String dosageForm, String strength, String presentation) {
        this.id = id;
        this.genericName = genericName;
        this.dosageForm = dosageForm;
        this.strength = strength;
        this.presentation = presentation;
    }

    public Integer getId() {
        return id;
    }

    public String getGenericName() {
        return genericName;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public String getStrength() {
        return strength;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }
}