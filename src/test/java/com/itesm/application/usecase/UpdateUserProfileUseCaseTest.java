package com.itesm.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.itesm.application.dto.UpdateUserDto;
import com.itesm.application.dto.UserProfileDto;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.application.security.CurrentUser;
import com.itesm.domain.models.Address;
import com.itesm.domain.models.Role;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class UpdateUserProfileUseCaseTest {

    private UserRepository userRepository;
    private AuthenticatedUserContext authUserContext;
    private UpdateUserProfileUseCase useCase;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        authUserContext = mock(AuthenticatedUserContext.class);

        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setName("Old");
        currentUser.setLastName1("Name");
        currentUser.setRole(new Role((byte) 3, "citizen"));
        when(authUserContext.getCurrentUser()).thenReturn(new CurrentUser(currentUser));

        useCase = new UpdateUserProfileUseCase(userRepository, authUserContext);
    }

    @Test
    public void execute_shouldUpdateAllProvidedFields() {
        User existing = createExistingUser();
        when(userRepository.findDomainById(1L)).thenReturn(Optional.of(existing));

        UpdateUserDto dto = new UpdateUserDto();
        dto.setName("NewName");
        dto.setLastName1("NewLastName1");
        dto.setLastName2("NewLastName2");
        dto.setAge((byte) 35);
        dto.setSuburbId(2);

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("NewName");
        updatedUser.setLastName1("NewLastName1");
        updatedUser.setLastName2("NewLastName2");
        updatedUser.setAge((byte) 35);
        updatedUser.setEmail("citizen@test.com");
        updatedUser.setRole(new Role((byte) 3, "citizen"));
        updatedUser.setActive(true);
        updatedUser.setAddress(new Address(2));
        when(userRepository.update(any(User.class))).thenReturn(updatedUser);

        UserProfileDto result = useCase.execute(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("NewName", result.getName());
        assertEquals("NewLastName1", result.getLastName1());
        assertEquals("NewLastName2", result.getLastName2());
        assertEquals((byte) 35, result.getAge());
        assertNotNull(result.getSuburb());

        verify(userRepository).findDomainById(1L);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).update(captor.capture());
        User passed = captor.getValue();
        assertEquals("NewName", passed.getName());
        assertEquals("NewLastName1", passed.getLastName1());
        assertEquals("NewLastName2", passed.getLastName2());
        assertEquals((byte) 35, passed.getAge());
        assertEquals(Integer.valueOf(2), passed.getAddress().getSuburbId());
    }

    @Test
    public void execute_shouldOnlyUpdateProvidedFields() {
        User existing = createExistingUser();
        when(userRepository.findDomainById(1L)).thenReturn(Optional.of(existing));

        UpdateUserDto dto = new UpdateUserDto();
        dto.setName("OnlyName");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("OnlyName");
        updatedUser.setLastName1("Existing1");
        updatedUser.setLastName2("Existing2");
        updatedUser.setAge((byte) 30);
        updatedUser.setEmail("citizen@test.com");
        updatedUser.setRole(new Role((byte) 3, "citizen"));
        updatedUser.setActive(true);
        when(userRepository.update(any(User.class))).thenReturn(updatedUser);

        UserProfileDto result = useCase.execute(dto);

        assertNotNull(result);
        assertEquals("OnlyName", result.getName());
        assertEquals("Existing2", result.getLastName2());
        assertEquals((byte) 30, result.getAge());
        assertNull(result.getSuburb());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).update(captor.capture());
        User passed = captor.getValue();
        assertEquals("OnlyName", passed.getName());
        assertEquals("Existing1", passed.getLastName1());
        assertEquals("Existing2", passed.getLastName2());
        assertEquals((byte) 30, passed.getAge());
        assertNull(passed.getAddress());
    }

    @Test
    public void execute_shouldReturnNullWhenUserNotFound() {
        when(userRepository.findDomainById(1L)).thenReturn(Optional.empty());

        UserProfileDto result = useCase.execute(new UpdateUserDto());

        assertNull(result);
        verify(userRepository, never()).update(any());
    }

    @Test
    public void execute_shouldReturnCurrentProfileWhenNoFieldsProvided() {
        User existing = createExistingUser();
        when(userRepository.findDomainById(1L)).thenReturn(Optional.of(existing));

        User updatedUser = copyUser(existing);
        when(userRepository.update(any(User.class))).thenReturn(updatedUser);

        UpdateUserDto dto = new UpdateUserDto();

        UserProfileDto result = useCase.execute(dto);

        assertNotNull(result);
        assertEquals("Citizen", result.getName());
        assertEquals("Existing2", result.getLastName2());
        assertEquals((byte) 30, result.getAge());
        assertNull(result.getSuburb());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).update(captor.capture());
        User passed = captor.getValue();
        assertEquals("Citizen", passed.getName());
        assertEquals("Existing1", passed.getLastName1());
    }

    private User createExistingUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Citizen");
        user.setLastName1("Existing1");
        user.setLastName2("Existing2");
        user.setAge((byte) 30);
        user.setEmail("citizen@test.com");
        user.setProviderUuid("citizen-token");
        user.setActive(true);
        user.setRole(new Role((byte) 3, "citizen"));
        return user;
    }

    private User copyUser(User original) {
        User u = new User();
        u.setId(original.getId());
        u.setName(original.getName());
        u.setLastName1(original.getLastName1());
        u.setLastName2(original.getLastName2());
        u.setAge(original.getAge());
        u.setEmail(original.getEmail());
        u.setProviderUuid(original.getProviderUuid());
        u.setActive(original.isActive());
        u.setRole(original.getRole());
        return u;
    }
}
