package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockAveragesResponse {
    @JsonProperty("last_month_avg")
    private BigDecimal lastMonthAvg;

    @JsonProperty("current_month_avg")
    private BigDecimal currentMonthAvg;
}
