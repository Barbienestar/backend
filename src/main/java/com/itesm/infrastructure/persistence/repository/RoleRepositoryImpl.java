package com.itesm.infrastructure.persistence.repository;

import com.itesm.infrastructure.persistence.entity.RoleEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import javax.management.relation.Role;
import java.util.Optional;

@ApplicationScoped
public class RoleRepositoryImpl implements PanacheRepositoryBase<RoleEntity, Byte> {

}