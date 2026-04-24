package com.itesm.application.usecase;

import com.itesm.application.dto.UserProfileDto;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;
import java.util.UUID;

/**
 * GetUserProfileUseCase
 */
@ApplicationScoped
public class GetUserProfileUseCase {
    private UserRepository userRepository;
    private AuthenticatedUserContext authUserContext;

    @Inject
    public GetUserProfileUseCase(
        UserRepository userRepository, AuthenticatedUserContext authUserContext) {
        this.userRepository = userRepository;
        this.authUserContext = authUserContext;
    }

    public UserProfileDto execute() {
        UUID userId = authUserContext.getCurrentUser().getId();
        Optional<User> userOptional = userRepository.find(userId);
        if (userOptional.isEmpty()) {
            return null;
        }
        User user = userOptional.get();
        return new UserProfileDto(
            user.getId(), user.getName(), user.getLastName1(), user.getRole(), user.getEmail());
    }
}
