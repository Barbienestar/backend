package com.itesm.domain.models;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicinesHospitalsStockAverages {
    @JsonProperty("last_month_avg")
    private BigDecimal lastMonthAvg;
    @JsonProperty("current_month_avg")
    private BigDecimal currentMonthAvg;
}
