package com.itesm.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Supply level data for a Mexican state")
public class StateSupplyDto {

    @Schema(description = "State identifier")
    private Byte stateId;

    @Schema(description = "State name")
    private String stateName;

    @Schema(description = "Average stock percentage (0-100)")
    private Double avgStock;

    @Schema(description = "Supply level: CA-01 (≥75%), CA-02 (50-74%), CA-03 (25-49%), CA-04 (<25%)")
    private String level;
}
