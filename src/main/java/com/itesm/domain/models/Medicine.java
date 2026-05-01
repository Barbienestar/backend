package com.itesm.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medicine {
    private Integer id;

    private String genericName;

    private String dosageForm;

    private String strength;

    private String presentation;
}