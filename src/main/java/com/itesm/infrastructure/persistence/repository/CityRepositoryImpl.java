package com.itesm.infrastructure.persistence.repository;

import java.util.List;
import java.util.stream.Collectors;

import com.itesm.domain.models.City;
import com.itesm.domain.repository.CityRepository;
import com.itesm.infrastructure.mapper.CityMapper;
import com.itesm.infrastructure.persistence.entity.CityEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@ApplicationScoped
public class CityRepositoryImpl implements CityRepository, PanacheRepositoryBase<CityEntity, Byte> {
    @Inject
    EntityManager em;

    @Override
    public List<City> getCitiesByState(Byte idState) {
        TypedQuery<CityEntity> query = em.createQuery(
            "SELECT c FROM CityEntity c WHERE c.idState.id = :idState",
            CityEntity.class
        );
        query.setParameter("idState", idState);
        return query.getResultList()
                .stream()
                .map(CityMapper::toDomain)
                .collect(Collectors.toList());
    }
}
