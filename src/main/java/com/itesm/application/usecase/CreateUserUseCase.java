package com.itesm.application.usecase;

import com.itesm.application.dto.CreateUserDto;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.UserRepository;
import com.itesm.domain.repository.UserTokenService;
import com.itesm.infrastructure.persistence.entity.RoleEntity;
import com.itesm.infrastructure.persistence.entity.SuburbEntity;
import com.itesm.infrastructure.persistence.entity.UserEntity;
import com.itesm.infrastructure.persistence.repository.RoleRepositoryImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDateTime;

@ApplicationScoped
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final UserTokenService userTokenService;

    @Inject
    public CreateUserUseCase(UserRepository userRepository,
                             UserTokenService userTokenService) {
        this.userRepository = userRepository;
        this.userTokenService = userTokenService;
    }

    public User execute(CreateUserDto dto) {
        // Crear usuario en Firebase y obtener UID
        String providerUuid = userTokenService.createUser(dto.getEmail(), dto.getPassword());

        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setName(dto.getName());
        user.setLastName1(dto.getLastName1());
        user.setLastName2(dto.getLastName2());
        user.setAge(dto.getAge());
        user.setEmail(dto.getEmail());
        user.setProviderUuid(providerUuid);
        user.setActive(true);
        user.setRole(dto.getRole());
        user.setSuburbId(dto.getSuburbId());
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        return userRepository.save(user);
    }
}