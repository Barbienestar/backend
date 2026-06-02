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
import com.itesm.domain.models.Role;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.UserRepository;
import com.itesm.domain.repository.UserTokenService;
import com.itesm.infrastructure.security.EncryptorConverter; // NUEVA IMPORTACIÓN
import jakarta.ws.rs.ForbiddenException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class CreateUserUseCaseTest {

    private UserRepository userRepository;
    private UserTokenService userTokenService;
    private AuthenticatedUserContext authUserContext;
    private CreationValidationStrategy validationStrategy;
    private EncryptorConverter encryptorConverter;
    private CreateUserUseCase useCase;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        userTokenService = mock(UserTokenService.class);
        authUserContext = mock(AuthenticatedUserContext.class);
        validationStrategy = mock(CreationValidationStrategy.class);
        encryptorConverter = mock(EncryptorConverter.class);

        useCase = new CreateUserUseCase(
                userRepository, userTokenService, authUserContext, validationStrategy, encryptorConverter);
    }

    @Test
    public void execute_shouldCreateCitizenUser() {
        CreateUserDto dto = new CreateUserDto(
                "Juan", "Perez", "Lopez", (byte) 30, "juan@test.com", "password123", (byte) 3, null, null);

        when(encryptorConverter.convertToDatabaseColumn("Juan")).thenReturn("ENC:JuanCifrado");
        when(encryptorConverter.convertToDatabaseColumn("Perez")).thenReturn("ENC:PerezCifrado");
        when(encryptorConverter.convertToDatabaseColumn("Lopez")).thenReturn("ENC:LopezCifrado");
        when(encryptorConverter.convertToDatabaseColumn("juan@test.com")).thenReturn("ENC:EmailCifrado");

        when(encryptorConverter.convertToEntityAttribute("ENC:JuanCifrado")).thenReturn("Juan");
        when(encryptorConverter.convertToEntityAttribute("ENC:PerezCifrado")).thenReturn("Perez");
        when(encryptorConverter.convertToEntityAttribute("ENC:LopezCifrado")).thenReturn("Lopez");
        when(encryptorConverter.convertToEntityAttribute("ENC:EmailCifrado")).thenReturn("juan@test.com");

        when(authUserContext.getCurrentUser()).thenReturn(new CurrentUser(createAdminUser()));
        when(userTokenService.createUser(dto.getEmail(), dto.getPassword())).thenReturn("provider-uuid-123");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
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
        assertEquals("Lopez", result.getLastName2());
        assertEquals((byte) 30, result.getAge());
        assertNull(result.getSuburb());

        verify(validationStrategy).setValidator(any(CitizenCreationValidator.class));
        verify(validationStrategy).validate(any(CreateUserDto.class), any(CurrentUser.class));
        verify(userTokenService).createUser("juan@test.com", "password123");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("ENC:JuanCifrado", savedUser.getName());
        assertEquals("ENC:PerezCifrado", savedUser.getLastName1());
        assertEquals("ENC:LopezCifrado", savedUser.getLastName2());
        assertEquals("ENC:EmailCifrado", savedUser.getEmail());

        assertEquals((byte) 30, savedUser.getAge());
        assertEquals("provider-uuid-123", savedUser.getProviderUuid());
        assertTrue(savedUser.isActive());
        assertEquals(Byte.valueOf((byte) 3), savedUser.getRole().getId());
        assertNotNull(savedUser.getAddress());
        assertNull(savedUser.getAddress().getAddress());
        assertNull(savedUser.getAddress().getSuburbId());
        assertNull(savedUser.getHospitals());
    }

    @Test
    public void execute_shouldCreatePrivilegedUserWhenCurrentUserIsAdmin() {
        CreateUserDto dto = new CreateUserDto(
                "AdminCreated", "User", "", (byte) 25, "newadmin@test.com", "pass123", (byte) 1, null, List.of(1, 2));

        when(authUserContext.getCurrentUser()).thenReturn(new CurrentUser(createAdminUser()));
        when(userTokenService.createUser(dto.getEmail(), dto.getPassword())).thenReturn("provider-uuid-456");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(2L);
            return user;
        });

        UserProfileDto result = useCase.execute(dto);

        assertNotNull(result);
        assertEquals(2L, result.getId());

        verify(validationStrategy).setValidator(any(PrivilegedCreationValidator.class));
        verify(validationStrategy).validate(any(CreateUserDto.class), any(CurrentUser.class));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User saved = userCaptor.getValue();

        assertEquals("AdminCreated", saved.getName());
        assertEquals("User", saved.getLastName1());
        assertEquals("newadmin@test.com", saved.getEmail());

        assertEquals(2, saved.getHospitals().size());
        assertTrue(saved.getHospitals().stream().anyMatch(h -> h.getId() == 1));
        assertTrue(saved.getHospitals().stream().anyMatch(h -> h.getId() == 2));

        verifyNoInteractions(encryptorConverter);
    }

    @Test
    public void execute_shouldThrowForbiddenWhenNonAdminCreatesPrivilegedUser() {
        CreateUserDto dto = new CreateUserDto(
                "Hacker", "Malicious", "", (byte) 20, "hacker@test.com", "hack123", (byte) 1, null, null);

        when(authUserContext.getCurrentUser()).thenReturn(new CurrentUser(createCitizenUser()));
        doThrow(new ForbiddenException("Only admins can create privileged users"))
                .when(validationStrategy)
                .validate(any(CreateUserDto.class), any(CurrentUser.class));

        assertThrows(ForbiddenException.class, () -> useCase.execute(dto));

        verify(validationStrategy).setValidator(any(PrivilegedCreationValidator.class));
        verify(validationStrategy).validate(any(CreateUserDto.class), any(CurrentUser.class));
        verifyNoInteractions(userTokenService);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(encryptorConverter);
    }

    @Test
    public void execute_shouldSetAddressWhenSuburbIdProvided() {
        CreateUserDto dto = new CreateUserDto(
                "Juan", "Perez", "Lopez", (byte) 30, "juan@test.com", "password123", (byte) 3, 5, null);

        when(authUserContext.getCurrentUser()).thenReturn(new CurrentUser(createAdminUser()));
        when(userTokenService.createUser(anyString(), anyString())).thenReturn("provider-uuid-789");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(3L);
            return user;
        });

        useCase.execute(dto);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertNotNull(saved.getAddress());
        assertEquals(Integer.valueOf(5), saved.getAddress().getSuburbId());
    }

    @Test
    public void execute_shouldSetHospitalsWhenHospitalIdsProvided() {
        CreateUserDto dto = new CreateUserDto(
                "Juan", "Perez", "Lopez", (byte) 30, "juan@test.com", "password123", (byte) 3, null, List.of(3, 4, 5));

        when(authUserContext.getCurrentUser()).thenReturn(new CurrentUser(createAdminUser()));
        when(userTokenService.createUser(anyString(), anyString())).thenReturn("provider-uuid-101");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(4L);
            return user;
        });

        useCase.execute(dto);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertNotNull(saved.getHospitals());
        assertEquals(3, saved.getHospitals().size());
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

    private User createCitizenUser() {
        User user = new User();
        user.setId(2L);
        user.setName("Citizen");
        user.setLastName1("User");
        user.setRole(new Role((byte) 3, "citizen"));
        user.setEmail("citizen@test.com");
        return user;
    }
}
