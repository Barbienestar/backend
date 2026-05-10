package com.itesm.infrastructure.persistence.repository;

import java.util.List;
import java.util.stream.Collectors;

import com.itesm.domain.models.Suburb;
import com.itesm.domain.repository.SuburbRepository;
import com.itesm.infrastructure.mapper.SuburbMapper;
import com.itesm.infrastructure.persistence.entity.SuburbEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@ApplicationScoped
public class SuburbRepositoryImpl implements SuburbRepository, PanacheRepositoryBase<SuburbEntity, Integer>{
    @Inject
    EntityManager em;
    
    @Override
    public List<Suburb> getSuburbsByCity(Integer idCity) {
        TypedQuery<SuburbEntity> query = em.createQuery(
            "SELECT s FROM SuburbEntity s WHERE s.idCity = :idCity",
            SuburbEntity.class
        );
        query.setParameter("idCity", idCity);
        return query.getResultList()
                .stream()
                .map(SuburbMapper::toDomain)
                .collect(Collectors.toList());
    }
}
