package com.itesm.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Hospital registered in the platform")
public class HospitalDto {

    @Schema(description = "Hospital identifier")
    private Integer id;

    @Schema(description = "Hospital name")
    private String name;
}
