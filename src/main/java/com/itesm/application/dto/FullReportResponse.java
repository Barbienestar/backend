package com.itesm.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/** FullReportResponse */
@Data
@AllArgsConstructor
public class FullReportResponse {
    private String description;
    private String imageUrl;
    private String userFullName;
    private String medicineName;
    private String medicinePresentation;
    private String medicineDosageForm;
    private String hospitalName;
}
