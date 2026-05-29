package com.itesm.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.itesm.application.dto.UserProfileDto;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.domain.models.Role;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.UserRepository;
import com.itesm.domain.repository.UserTokenService;
import com.itesm.domain.repository.UserTokenService.TokenVerification;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotAuthorizedException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class GoogleSignInUseCaseTest {

    private UserTokenService userTokenService;
    private UserRepository userRepository;
    private AuthenticatedUserContext authUserContext;
    private GoogleSignInUseCase useCase;

    @BeforeEach
    void setup() {
        userTokenService = mock(UserTokenService.class);
        userRepository = mock(UserRepository.class);
        authUserContext = mock(AuthenticatedUserContext.class);
        useCase = new GoogleSignInUseCase(userTokenService, userRepository, authUserContext);
    }

    @Test
    void execute_shouldAutoProvisionCitizen_whenFirstTimeGoogleUser() {
        when(userTokenService.verifyIdToken("google-token-123"))
                .thenReturn(new TokenVerification("firebase-uid-abc", "ana@google.com", "Ana García"));
        when(userRepository.findByProviderUuid("firebase-uid-abc")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(10L);
            return user;
        });

        UserProfileDto result = useCase.execute("Bearer google-token-123");

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Ana", result.getName());
        assertEquals("García", result.getLastName1());
        assertEquals("ana@google.com", result.getEmail());
        assertEquals("citizen", result.getRole());
        assertNull(result.getAge());

        verify(authUserContext).setCurrentUser(any());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User savedUser = captor.getValue();
        assertEquals("citizen", savedUser.getRole().getName());
    }

    @Test
    void execute_shouldReturnExistingProfile_whenCitizenAlreadyExists() {
        User existing = new User();
        existing.setId(3L);
        existing.setName("Citizen");
        existing.setLastName1("User");
        existing.setEmail("citizen@test.com");
        existing.setProviderUuid("citizen-token");
        existing.setActive(true);
        existing.setRole(new Role((byte) 3, "citizen"));

        when(userTokenService.verifyIdToken("citizen-token"))
                .thenReturn(new TokenVerification("citizen-token", "citizen@test.com", "Citizen User"));
        when(userRepository.findByProviderUuid("citizen-token")).thenReturn(Optional.of(existing));

        UserProfileDto result = useCase.execute("Bearer citizen-token");

        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("citizen", result.getRole());

        verify(authUserContext).setCurrentUser(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void execute_shouldThrow403_whenNonCitizenUsesGoogle() {
        User admin = new User();
        admin.setId(1L);
        admin.setName("Admin");
        admin.setLastName1("User");
        admin.setEmail("admin@test.com");
        admin.setProviderUuid("admin-token");
        admin.setActive(true);
        admin.setRole(new Role((byte) 1, "admin"));

        when(userTokenService.verifyIdToken("admin-token"))
                .thenReturn(new TokenVerification("admin-token", "admin@test.com", "Admin User"));
        when(userRepository.findByProviderUuid("admin-token")).thenReturn(Optional.of(admin));

        assertThrows(ForbiddenException.class, () -> useCase.execute("Bearer admin-token"));

        verify(authUserContext, never()).setCurrentUser(any());
    }

    @Test
    void execute_shouldThrow401_whenTokenInvalid() {
        when(userTokenService.verifyIdToken("bad-token")).thenThrow(new RuntimeException("Invalid token"));

        assertThrows(NotAuthorizedException.class, () -> useCase.execute("Bearer bad-token"));

        verifyNoInteractions(userRepository);
        verifyNoInteractions(authUserContext);
    }

    @Test
    void execute_shouldThrow401_whenAuthHeaderMissing() {
        assertThrows(NotAuthorizedException.class, () -> useCase.execute(null));

        verifyNoInteractions(userTokenService);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(authUserContext);
    }

    @Test
    void execute_shouldThrow401_whenAuthHeaderNotBearer() {
        assertThrows(NotAuthorizedException.class, () -> useCase.execute("Basic some-token"));

        verifyNoInteractions(userTokenService);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(authUserContext);
    }

    @Test
    void execute_shouldHandleSingleNameGoogleProfile() {
        when(userTokenService.verifyIdToken("single-token"))
                .thenReturn(new TokenVerification("uid-single", "single@google.com", "Mono"));
        when(userRepository.findByProviderUuid("uid-single")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(11L);
            user.setRole(new Role((byte) 3, "citizen"));
            return user;
        });

        UserProfileDto result = useCase.execute("Bearer single-token");

        assertEquals("Mono", result.getName());
        assertEquals("Mono", result.getLastName1());
    }
}
