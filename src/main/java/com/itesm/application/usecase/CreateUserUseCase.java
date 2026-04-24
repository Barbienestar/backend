package com.itesm.application.usecase;

import com.itesm.application.dto.CreateUserDto;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.UserRepository;
import com.itesm.domain.repository.UserTokenService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.UUID;

/**
 * CreateUserUseCase
 */
@ApplicationScoped
public class CreateUserUseCase {
    private UserRepository userRepository;
    private UserTokenService userTokenService;

    @Inject
    public CreateUserUseCase(UserRepository userRepository, UserTokenService userTokenService) {
        this.userRepository = userRepository;
        this.userTokenService = userTokenService;
    }

    public User execute(CreateUserDto createUserDto) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(createUserDto.getName());
        user.setLastName1(createUserDto.getLastName1());
        user.setLastName2(createUserDto.getLastName2());
        user.setEmail(createUserDto.getEmail());
        user.setRole(createUserDto.getRole());
        user.setActive(true);

        String providerUid =
            userTokenService.createUser(createUserDto.getEmail(), createUserDto.getPassword());
        user.setProviderUid(providerUid);
        return userRepository.save(user);
    }
}
