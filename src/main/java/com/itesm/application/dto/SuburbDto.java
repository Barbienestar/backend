package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Suburb (colonia) belonging to a city")
public class SuburbDto {

    @Schema(description = "Suburb identifier")
    private Integer id;

    @Schema(description = "Suburb name")
    private String name;

    @JsonProperty("zip_code")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Postal code", name = "zip_code")
    private String zipCode;
}
