package com.itesm.application.usecase;

import com.itesm.application.dto.SuburbDto;
import com.itesm.application.dto.UserProfileDto;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.application.security.CurrentUser;
import com.itesm.domain.exceptions.InvalidTokenException;
import com.itesm.domain.models.Role;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.UserRepository;
import com.itesm.domain.repository.UserTokenService;
import com.itesm.domain.repository.UserTokenService.TokenVerification;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class GoogleSignInUseCase {

    private static final String KEY_MESSAGE = "message";

    private final UserTokenService userTokenService;
    private final UserRepository userRepository;
    private final AuthenticatedUserContext authUserContext;

    @Inject
    public GoogleSignInUseCase(
            UserTokenService userTokenService,
            UserRepository userRepository,
            AuthenticatedUserContext authUserContext) {
        this.userTokenService = userTokenService;
        this.userRepository = userRepository;
        this.authUserContext = authUserContext;
    }

    public UserProfileDto execute(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of(KEY_MESSAGE, "Token not found"))
                    .type(MediaType.APPLICATION_JSON)
                    .build());
        }

        String idToken = authHeader.replace("Bearer ", "");
        TokenVerification verification;
        try {
            verification = userTokenService.verifyIdToken(idToken);
        } catch (InvalidTokenException e) {
            throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of(KEY_MESSAGE, "Invalid Google token"))
                    .type(MediaType.APPLICATION_JSON)
                    .build());
        }

        Optional<User> existing = userRepository.findByProviderUuid(verification.uid());

        User user;
        if (existing.isPresent()) {
            user = existing.get();
            if (!"citizen".equals(user.getRole().getName())) {
                throw new ForbiddenException(Response.status(Response.Status.FORBIDDEN)
                        .entity(Map.of(KEY_MESSAGE, "Google sign-in is only available for citizen accounts"))
                        .type(MediaType.APPLICATION_JSON)
                        .build());
            }
        } else {
            user = new User();
            user.setName(splitFirstName(verification.name()));
            user.setLastName1(splitLastName(verification.name()));
            user.setEmail(verification.email());
            user.setProviderUuid(verification.uid());
            user.setActive(true);
            user.setRole(new Role((byte) 3, "citizen"));

            user = userRepository.save(user);
        }

        authUserContext.setCurrentUser(new CurrentUser(user));

        return toProfileDto(user);
    }

    private String splitFirstName(String displayName) {
        if (displayName == null || displayName.isBlank()) return "User";
        int space = displayName.indexOf(' ');
        return space == -1 ? displayName : displayName.substring(0, space);
    }

    private String splitLastName(String displayName) {
        if (displayName == null || displayName.isBlank()) return "User";
        int space = displayName.indexOf(' ');
        return space == -1 ? displayName : displayName.substring(space + 1).trim();
    }

    private UserProfileDto toProfileDto(User user) {
        SuburbDto suburb = null;
        if (user.getAddress() != null && user.getAddress().getSuburbId() != null) {
            suburb = new SuburbDto(
                    user.getAddress().getSuburbId(), user.getAddress().getAddressName(), null);
        }
        return new UserProfileDto(
                user.getId(),
                user.getName(),
                user.getLastName1(),
                user.getLastName2(),
                user.getAge(),
                suburb,
                user.getRole().getName(),
                user.getEmail());
    }
}
