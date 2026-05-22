package com.itesm.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users_Hospitals")
public class UserHospitalEntity {

    @EmbeddedId
    private UserHospitalId id;

    @ManyToOne
    @MapsId("idUser")
    @JoinColumn(name = "id_user")
    private UserEntity user;

    @ManyToOne
    @MapsId("idHospital")
    @JoinColumn(name = "id_hospital")
    private HospitalEntity hospital;
}
