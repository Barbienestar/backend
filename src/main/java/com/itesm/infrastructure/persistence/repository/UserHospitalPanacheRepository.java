package com.itesm.infrastructure.persistence.repository;
import com.itesm.infrastructure.persistence.entity.UserHospitalEntity;
import com.itesm.infrastructure.persistence.entity.UserHospitalId;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserHospitalPanacheRepository implements PanacheRepositoryBase<UserHospitalEntity, UserHospitalId> {
}