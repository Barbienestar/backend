package com.itesm.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    // CORE
    private Long id;

    private Long userId;

    private Integer medicineId;

    private Integer hospitalId;

    private Byte statusId;

    private String description;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // EXTRA
    private User user;
    private Medicine medicine;
    private Hospital hospital;
}
