package com.itesm.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medicine {
    private Integer id;

    private String genericName;

    private String dosageForm;

    private String strength;

    private String presentation;

    // Constructor sin ID para creación de nuevos medicamentos
    public Medicine(String genericName, String dosageForm, String strength, String presentation) {
    this.genericName = genericName;
    this.dosageForm = dosageForm;
    this.strength = strength;
    this.presentation = presentation;
}
}