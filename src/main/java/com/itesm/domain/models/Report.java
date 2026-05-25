package com.itesm.domain.models;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
