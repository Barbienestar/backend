package com.itesm.infrastructure.persistence.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Medicines_Hospitals")
public class MedicineHospitalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_medicine")
    private MedicineEntity medicine;

    @ManyToOne
    @JoinColumn(name = "id_hospital")
    private HospitalEntity hospital;

    @Column(name = "stock")
    private int stock;

    @Column(name = "entry_date")
    private LocalDateTime entryDate;
}