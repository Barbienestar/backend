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
@Table(name = "Medicines_Hospitals")
public class MedicinesHospitalsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medicine", nullable = false)
    private MedicineEntity medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hospital", nullable = false)
    private HospitalEntity hospital;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "entry_date", nullable = false)
    private LocalDateTime entryDate;
}