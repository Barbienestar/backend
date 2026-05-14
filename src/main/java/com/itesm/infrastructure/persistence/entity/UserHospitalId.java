package com.itesm.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import jakarta.persistence.Column;

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
