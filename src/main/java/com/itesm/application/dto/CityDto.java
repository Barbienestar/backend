package com.itesm.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "City belonging to a Mexican state")
public class CityDto {

    @Schema(description = "City identifier")
    private Integer id;

    @Schema(description = "City name")
    private String name;
}
