package com.itesm.application.usecase;

import com.itesm.application.dto.UpdateUserDto;
import com.itesm.application.dto.UserProfileDto;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.domain.models.Address;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UpdateUserProfileUseCase {

    private final UserRepository userRepository;
    private final AuthenticatedUserContext authUserContext;

    @Inject
    public UpdateUserProfileUseCase(UserRepository userRepository, AuthenticatedUserContext authUserContext) {
        this.userRepository = userRepository;
        this.authUserContext = authUserContext;
    }

    public UserProfileDto execute(UpdateUserDto dto) {
        Long userId = authUserContext.getCurrentUser().getId();
        User user = userRepository.findDomainById(userId).orElse(null);
        if (user == null) return null;

        applyUpdates(user, dto);
        User savedUser = userRepository.update(user);

        return new UserProfileDto(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getLastName1(),
                savedUser.getRole().getName(),
                savedUser.getEmail());
    }

    private void applyUpdates(User user, UpdateUserDto dto) {
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getLastName1() != null) user.setLastName1(dto.getLastName1());
        if (dto.getLastName2() != null) user.setLastName2(dto.getLastName2());
        if (dto.getAge() != null) user.setAge(dto.getAge());
        if (dto.getSuburbId() != null) {
            user.setAddress(new Address(dto.getSuburbId()));
        }
    }
}
