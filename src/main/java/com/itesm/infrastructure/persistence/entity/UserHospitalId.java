package com.itesm.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserHospitalId implements Serializable {

    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "id_hospital")
    private Integer idHospital;
}
