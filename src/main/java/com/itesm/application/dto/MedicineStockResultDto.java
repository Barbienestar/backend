package com.itesm.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Result of a medicine stock CSV upload")
public class MedicineStockResultDto {

    @Schema(description = "Number of stock records successfully inserted or updated")
    private int inserted;

    @Schema(description = "List of error messages for rows that could not be processed")
    private List<String> errors;
}
