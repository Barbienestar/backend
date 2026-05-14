package com.itesm.infrastructure.persistence.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Medicines")
public class MedicineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "generic_name", nullable = false)
    private String genericName;

    @Column(name = "dosage_form", nullable = false)
    private String dosageForm;

    @Column(name = "strength")
    private String strength;

    @Column(name = "presentation", length = 512)
    private String presentation;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

