package com.itesm.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Reports_Snapshot")
public class ReportsSnapshotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "daily_reports", nullable = false)
    private Integer dailyReports;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hospital", nullable = false)
    private HospitalEntity hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medicine", nullable = false)
    private MedicineEntity medicine;

    @Column(name = "snapshot_date", nullable = false)
    private LocalDate snapshotDate;
}
