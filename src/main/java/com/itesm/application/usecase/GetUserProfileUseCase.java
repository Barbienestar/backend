package com.itesm.application.usecase;

import com.itesm.application.dto.SuburbDto;
import com.itesm.application.dto.UserProfileDto;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

/** GetUserProfileUseCase */
@ApplicationScoped
public class GetUserProfileUseCase {
    private UserRepository userRepository;
    private AuthenticatedUserContext authUserContext;

    @Inject
    public GetUserProfileUseCase(UserRepository userRepository, AuthenticatedUserContext authUserContext) {
        this.userRepository = userRepository;
        this.authUserContext = authUserContext;
    }

    public UserProfileDto execute() {
        Long userId = authUserContext.getCurrentUser().getId();
        Optional<User> userOptional = userRepository.findDomainById(userId);
        if (userOptional.isEmpty()) {
            return null;
        }

        User user = userOptional.get();
        SuburbDto suburb = null;
        if (user.getAddress() != null && user.getAddress().getSuburbId() != null) {
            suburb = new SuburbDto(user.getAddress().getSuburbId(), user.getAddress().getAddress(), null);
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
