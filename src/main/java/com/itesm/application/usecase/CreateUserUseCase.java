package com.itesm.application.usecase;

import com.itesm.application.dto.CreateUserDto;
import com.itesm.application.dto.UserProfileDto;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.application.validation.CitizenCreationValidator;
import com.itesm.application.validation.CreationValidationStrategy;
import com.itesm.application.validation.PrivilegedCreationValidator;
import com.itesm.domain.models.Address;
import com.itesm.domain.models.Role;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.UserRepository;
import com.itesm.domain.repository.UserTokenService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final UserTokenService userTokenService;
    private final AuthenticatedUserContext authUserContext;
    private final CreationValidationStrategy validationStrategy;

    @Inject
    public CreateUserUseCase(
            UserRepository userRepository,
            UserTokenService userTokenService,
            AuthenticatedUserContext authUserContext,
            CreationValidationStrategy validationStrategy) {
        this.userRepository = userRepository;
        this.userTokenService = userTokenService;
        this.authUserContext = authUserContext;
        this.validationStrategy = validationStrategy;
    }

    public UserProfileDto execute(CreateUserDto dto) {
        if (dto.getRoleId() == 1 || dto.getRoleId() == 2) {
            validationStrategy.setValidator(new PrivilegedCreationValidator());
        } else {
            validationStrategy.setValidator(new CitizenCreationValidator());
        }
        validationStrategy.validate(dto, authUserContext.getCurrentUser());
        String providerUuid = userTokenService.createUser(dto.getEmail(), dto.getPassword());

        User user = new User();
        user.setName(dto.getName());
        user.setLastName1(dto.getLastName1());
        user.setLastName2(dto.getLastName2());
        user.setAge(dto.getAge());
        user.setEmail(dto.getEmail());
        user.setProviderUuid(providerUuid);
        user.setActive(true);
        user.setRole(new Role(dto.getRoleId()));
        user.setAddress(new Address(dto.getSuburbId()));

        User savedUser = userRepository.save(user);

        UserProfileDto userProfile =
                new UserProfileDto(
                        savedUser.getId(),
                        savedUser.getName(),
                        savedUser.getLastName1(),
                        savedUser.getRole().getName(),
                        savedUser.getEmail());

        return userProfile;
    }
}
