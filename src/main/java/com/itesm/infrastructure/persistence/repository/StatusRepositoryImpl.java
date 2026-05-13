package com.itesm.infrastructure.persistence.repository;

import com.itesm.domain.models.Status;
import com.itesm.domain.repository.StatusRepository;
import com.itesm.infrastructure.mapper.StatusMapper;
import com.itesm.infrastructure.persistence.entity.StatusEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class StatusRepositoryImpl implements StatusRepository, PanacheRepositoryBase<StatusEntity, Byte> {
    @Override
    public Status findStatusByName(String name) {
        StatusEntity entity = find("name", name).firstResult();
        if (entity == null) {
            return null;
        }
        return StatusMapper.toDomain(entity);
    }

    @Override
    public Status findStatusById(Byte id) {
        StatusEntity entity = findById(id);
        if (entity == null) {
            return null;
        }
        return StatusMapper.toDomain(entity);
    }
}
