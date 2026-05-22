package com.itesm.infrastructure.persistence.repository;

import com.itesm.domain.models.Hospital;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.UserRepository;
import com.itesm.infrastructure.mapper.UserMapper;
import com.itesm.infrastructure.persistence.entity.HospitalEntity;
import com.itesm.infrastructure.persistence.entity.RoleEntity;
import com.itesm.infrastructure.persistence.entity.SuburbEntity;
import com.itesm.infrastructure.persistence.entity.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class UserRepositoryImpl implements UserRepository, PanacheRepositoryBase<UserEntity, Long> {

    private final EntityManager em;

    @Inject
    public UserRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public User save(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        entity.setRole(em.getReference(RoleEntity.class, user.getRole().getId()));
        if (user.getAddress() != null && user.getAddress().getSuburbId() != null) {
            entity.setSuburb(
                    em.getReference(SuburbEntity.class, user.getAddress().getSuburbId()));
        }
        if (user.getHospitals() != null) {
            for (Hospital hospital : user.getHospitals()) {
                entity.getHospitals().add(em.getReference(HospitalEntity.class, hospital.getId()));
            }
        }
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        persist(entity);
        return UserMapper.toDomain(entity);
    }

    @Override
    @Transactional
    public Optional<User> findByProviderUuid(String providerUuid) {
        UserEntity entity = find("providerUuid", providerUuid).firstResult();
        return Optional.ofNullable(entity).map(UserMapper::toDomain);
    }

    @Override
    @Transactional
    public Optional<User> findDomainById(Long id) {
        UserEntity entity = findById(id);
        return Optional.ofNullable(entity).map(UserMapper::toDomain);
    }

    @Override
    @Transactional
    public User update(User user) {
        UserEntity entity = findById(user.getId());
        if (entity == null) return null;

        if (user.getName() != null) entity.setName(user.getName());
        if (user.getLastName1() != null) entity.setLastName1(user.getLastName1());
        if (user.getLastName2() != null) entity.setLastName2(user.getLastName2());
        if (user.getAge() != null) entity.setAge(user.getAge());
        if (user.getAddress() != null && user.getAddress().getSuburbId() != null) {
            entity.setSuburb(em.getReference(SuburbEntity.class, user.getAddress().getSuburbId()));
        }

        entity.setUpdatedAt(LocalDateTime.now());
        em.merge(entity);
        return UserMapper.toDomain(entity);
    }

    @Override
    public long countByRoleId(byte roleId) {
        return count("role.id", roleId);
    }
}
