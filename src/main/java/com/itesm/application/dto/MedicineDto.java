package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Medicine in the catalog")
public class MedicineDto {

    @Schema(description = "Medicine identifier")
    private Integer id;

    @JsonProperty("generic_name")
    @Schema(description = "Generic (INN) name of the medicine")
    private String genericName;

    @JsonProperty("dosage_form")
    @Schema(description = "Dosage form (e.g. tablet, solution, capsule)")
    private String dosageForm;

    @Schema(description = "Strength or concentration (e.g. 500 mg)")
    private String strength;

    @Schema(description = "Presentation / pack size (e.g. 20 tablets)")
    private String presentation;
}
