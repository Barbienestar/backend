package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PeriodReportsByHospitalResponse {
    @JsonProperty("report_date")
    private LocalDate date;

    @JsonProperty("total_accepted_reports")
    private int totalAcceptedReports;
}
