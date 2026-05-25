package com.itesm.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.itesm.domain.models.Role;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.RoleRepository;
import com.itesm.domain.repository.UserRepository;
import com.itesm.domain.repository.UserTokenService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class EnsureAdminUseCaseTest {

    private UserRepository userRepository;
    private UserTokenService userTokenService;
    private RoleRepository roleRepository;
    private EnsureAdminUseCase useCase;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        userTokenService = mock(UserTokenService.class);
        roleRepository = mock(RoleRepository.class);
        useCase = new EnsureAdminUseCase(
                userRepository, userTokenService, roleRepository, "admin@test.com", "password123", "Admin", "User");
    }

    @Test
    void execute_shouldDoNothing_whenAdminAlreadyExists() {
        Role adminRole = new Role((byte) 1, "admin");
        when(roleRepository.findByName("admin")).thenReturn(Optional.of(adminRole));
        when(userRepository.countByRoleId((byte) 1)).thenReturn(1L);

        useCase.execute();

        verify(userRepository).countByRoleId((byte) 1);
        verifyNoInteractions(userTokenService);
    }

    @Test
    void execute_shouldDoNothing_whenRoleNotFound() {
        when(roleRepository.findByName("admin")).thenReturn(Optional.empty());

        useCase.execute();

        verify(roleRepository).findByName("admin");
        verifyNoInteractions(userTokenService);
        verifyNoInteractions(userRepository);
    }

    @Test
    void execute_shouldCreateAdmin_whenNoneExists() {
        Role adminRole = new Role((byte) 1, "admin");
        when(roleRepository.findByName("admin")).thenReturn(Optional.of(adminRole));
        when(userRepository.countByRoleId((byte) 1)).thenReturn(0L);
        when(userTokenService.createUser("admin@test.com", "password123")).thenReturn("firebase-uid-abc");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        useCase.execute();

        verify(userTokenService).createUser("admin@test.com", "password123");
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertEquals("Admin", saved.getName());
        assertEquals("User", saved.getLastName1());
        assertEquals("admin@test.com", saved.getEmail());
        assertEquals("firebase-uid-abc", saved.getProviderUuid());
        assertTrue(saved.isActive());
        assertEquals(Byte.valueOf((byte) 1), saved.getRole().getId());
        assertNull(saved.getAddress());
    }
}
