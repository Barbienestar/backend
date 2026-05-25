package com.itesm.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Stock availability of a medicine at a specific hospital")
public class MedicinesHospitalsStockDto {

    @Schema(description = "Hospital identifier")
    private Integer hospitalId;

    @Schema(description = "Hospital name")
    private String hospitalName;

    @Schema(description = "Hospital address")
    private String address;

    @Schema(description = "Human-readable stock label (e.g. Alto, Medio, Bajo)")
    private String stockLabel;

    @Schema(description = "Availability status (e.g. Disponible, Sin stock)")
    private String status;

    @Schema(description = "Google Maps URL for the hospital location")
    private String mapsUrl;
}
