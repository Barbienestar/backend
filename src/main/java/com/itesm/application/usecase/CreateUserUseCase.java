package com.itesm.application.usecase;

import com.itesm.application.dto.CreateUserDto;
import com.itesm.application.dto.SuburbDto;
import com.itesm.application.dto.UserProfileDto;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.application.validation.CitizenCreationValidator;
import com.itesm.application.validation.CreationValidationStrategy;
import com.itesm.application.validation.PrivilegedCreationValidator;
import com.itesm.domain.exceptions.InvalidRoleException;
import com.itesm.domain.exceptions.UserCreationException;
import com.itesm.domain.models.Address;
import com.itesm.domain.models.Hospital;
import com.itesm.domain.models.Role;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.UserRepository;
import com.itesm.domain.repository.UserTokenService;
import com.itesm.infrastructure.security.EncryptorConverter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final UserTokenService userTokenService;
    private final AuthenticatedUserContext authUserContext;
    private final CreationValidationStrategy validationStrategy;
    private final EncryptorConverter encryptorConverter;

    @Inject
    public CreateUserUseCase(
            UserRepository userRepository,
            UserTokenService userTokenService,
            AuthenticatedUserContext authUserContext,
            CreationValidationStrategy validationStrategy,
            EncryptorConverter encryptorConverter) {
        this.userRepository = userRepository;
        this.userTokenService = userTokenService;
        this.authUserContext = authUserContext;
        this.validationStrategy = validationStrategy;
        this.encryptorConverter = encryptorConverter;
    }

    public UserProfileDto execute(CreateUserDto dto) {
        // Validate roleId is a valid number
        if (dto.getRoleId() != 1 && dto.getRoleId() != 2 && dto.getRoleId() != 3) {
            throw new InvalidRoleException(dto.getRoleId());
        }

        if (dto.getRoleId() == 1 || dto.getRoleId() == 2) {
            validationStrategy.setValidator(new PrivilegedCreationValidator());
        } else {
            validationStrategy.setValidator(new CitizenCreationValidator());
        }
        validationStrategy.validate(dto, authUserContext.getCurrentUser());

        String providerUuid = userTokenService.createUser(dto.getEmail(), dto.getPassword());

        try {
            User user = new User();
            if (dto.getRoleId() == 3) {
                user.setName(encryptorConverter.convertToDatabaseColumn(dto.getName()));
                user.setLastName1(encryptorConverter.convertToDatabaseColumn(dto.getLastName1()));
                user.setLastName2(encryptorConverter.convertToDatabaseColumn(dto.getLastName2()));
                user.setEmail(encryptorConverter.convertToDatabaseColumn(dto.getEmail()));
            } else {
                user.setName(dto.getName());
                user.setLastName1(dto.getLastName1());
                user.setLastName2(dto.getLastName2());
                user.setEmail(dto.getEmail());
            }

            user.setAge(dto.getAge());
            user.setProviderUuid(providerUuid);
            user.setActive(true);
            user.setRole(new Role(dto.getRoleId()));
            user.setAddress(new Address(dto.getSuburbId()));

            if (dto.getHospitalIds() != null) {
                user.setHospitals(
                        dto.getHospitalIds().stream().map(Hospital::new).toList());
            }

            User savedUser = userRepository.save(user);

            SuburbDto suburb = null;
            if (savedUser.getAddress() != null && savedUser.getAddress().getSuburbId() != null) {
                suburb = new SuburbDto(
                        savedUser.getAddress().getSuburbId(),
                        savedUser.getAddress().getAddress(),
                        null);
            }
            String responseName = (dto.getRoleId() == 3) ? encryptorConverter.convertToEntityAttribute(savedUser.getName()) : savedUser.getName();
            String responseLastName1 = (dto.getRoleId() == 3) ? encryptorConverter.convertToEntityAttribute(savedUser.getLastName1()) : savedUser.getLastName1();
            String responseLastName2 = (dto.getRoleId() == 3) ? encryptorConverter.convertToEntityAttribute(savedUser.getLastName2()) : savedUser.getLastName2();
            String responseEmail = (dto.getRoleId() == 3) ? encryptorConverter.convertToEntityAttribute(savedUser.getEmail()) : savedUser.getEmail();

            UserProfileDto userProfile = new UserProfileDto(
                    savedUser.getId(),
                    responseName,
                    responseLastName1,
                    responseLastName2,
                    savedUser.getAge(),
                    suburb,
                    savedUser.getRole().getName(),
                    responseEmail);

            return userProfile;

        } catch (Exception e) {
            userTokenService.deleteUser(providerUuid);
            throw new UserCreationException("User creation failed, changes have been rolled back.", e);
        }
    }
}