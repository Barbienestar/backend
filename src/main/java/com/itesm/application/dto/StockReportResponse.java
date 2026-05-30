package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockReportResponse {
    @JsonProperty("low_stock_count")
    private Integer lowStockCount;

    @JsonProperty("bottom_medicines")
    private List<String> bottomMedicines;
}
