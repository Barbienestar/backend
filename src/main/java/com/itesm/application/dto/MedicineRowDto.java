package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineRowDto {

    @JsonProperty("generic_name")
    private String genericName;
    @JsonProperty("dosage_form")
    private String dosageForm;
    private String strength;
    private String presentation;
    private int stock;
}