package com.itesm.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.QueryParam;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockAveragesDto {

    @NotNull
    @QueryParam("first_date")
    private LocalDate firstDate;

    @NotNull
    @QueryParam("second_date")
    private LocalDate secondDate;
}
