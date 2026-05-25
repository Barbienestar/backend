package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Medicine with a stock level at or below the critical threshold")
public class CriticalMedicineDto {

    @Schema(description = "Medicine identifier")
    private Integer id;

    @JsonProperty("generic_name")
    @Schema(description = "Generic name of the medicine")
    private String genericName;

    @Schema(description = "Current stock units available at the hospital")
    private int stock;
}
