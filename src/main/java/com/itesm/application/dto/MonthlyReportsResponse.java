package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyReportsResponse {
    @JsonProperty("current_month_report_count")
    Integer currentMonthReportCount;

    @JsonProperty("comparison_to_last_month")
    BigDecimal comparisonToLastMonth;
}
