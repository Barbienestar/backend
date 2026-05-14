package com.itesm.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.itesm.application.dto.CreateUserDto;
import com.itesm.application.dto.UserProfileDto;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.application.security.CurrentUser;
import com.itesm.application.validation.CitizenCreationValidator;
import com.itesm.application.validation.CreationValidationStrategy;
import com.itesm.application.validation.PrivilegedCreationValidator;
import com.itesm.domain.models.Address;
import com.itesm.domain.models.Hospital;
import com.itesm.domain.models.Role;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.UserRepository;
import com.itesm.domain.repository.UserTokenService;

import jakarta.ws.rs.ForbiddenException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateUserUseCaseTest {

    private UserRepository userRepository;
    private UserTokenService userTokenService;
    private AuthenticatedUserContext authUserContext;
    private CreationValidationStrategy validationStrategy;
    private CreateUserUseCase useCase;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        userTokenService = mock(UserTokenService.class);
        authUserContext = mock(AuthenticatedUserContext.class);
        validationStrategy = mock(CreationValidationStrategy.class);

        useCase = new CreateUserUseCase(
                userRepository, userTokenService, authUserContext, validationStrategy);
    }

    @Test
    public void execute_shouldCreateCitizenUser() {
        CreateUserDto dto = new CreateUserDto(
                "Juan", "Perez", "Lopez", 30,
                "juan@test.com", "password123", (byte) 3,
                null, null);

        when(authUserContext.getCurrentUser()).thenReturn(new CurrentUser(createAdminUser()));
        when(userTokenService.createUser(dto.getEmail(), dto.getPassword()))
                .thenReturn("provider-uuid-123");
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setId(1L);
                    return user;
                });

        UserProfileDto result = useCase.execute(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Juan", result.getName());
        assertEquals("Perez", result.getLastName1());
        assertNull(result.getRole());
        assertEquals("juan@test.com", result.getEmail());

        verify(validationStrategy).setValidator(any(CitizenCreationValidator.class));
        verify(validationStrategy).validate(any(CreateUserDto.class), any(CurrentUser.class));
        verify(userTokenService).createUser("juan@test.com", "password123");
        verify(userRepository).save(any(User.class));
    }

    private User createAdminUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Admin");
        user.setLastName1("User");
        user.setRole(new Role((byte) 1, "admin"));
        user.setEmail("admin@test.com");
        return user;
    }
}
