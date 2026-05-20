package com.itesm.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.itesm.application.dto.HospitalDto;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.application.security.CurrentUser;
import com.itesm.domain.models.Hospital;
import com.itesm.domain.models.Role;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.HospitalRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

/** GetMyHospitalsUseCaseTest */
public class GetMyHospitalsUseCaseTest {

    private HospitalRepository hospitalRepository;
    private AuthenticatedUserContext authUserContext;
    private GetMyHospitalsUseCase getMyHospitalsUseCase;

    @BeforeEach
    void setup() {
        hospitalRepository = mock(HospitalRepository.class);
        authUserContext = mock(AuthenticatedUserContext.class);

        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setRole(new Role((byte) 1, "citizen"));
        user.setLastName1("Pork");
        CurrentUser currentUser = new CurrentUser(user);
        when(authUserContext.getCurrentUser()).thenReturn(currentUser);

        getMyHospitalsUseCase = new GetMyHospitalsUseCase(hospitalRepository, authUserContext);
    }

    @Test
    public void execute_shouldReturnMappedHospitalsForCurrentUser() {
        Hospital hospitalA = new Hospital(1, "Hospital General", "https://maps/A");
        Hospital hospitalB = new Hospital(2, "Hospital del Norte", "https://maps/B");
        when(hospitalRepository.findHospitalsByUserId(1L))
                .thenReturn(List.of(hospitalA, hospitalB));

        List<HospitalDto> result = getMyHospitalsUseCase.execute();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Hospital General", result.get(0).getName());
        assertEquals(2, result.get(1).getId());
        assertEquals("Hospital del Norte", result.get(1).getName());
    }

    @Test
    public void execute_shouldReturnEmptyListWhenUserHasNoHospitals() {
        when(hospitalRepository.findHospitalsByUserId(1L)).thenReturn(List.of());

        List<HospitalDto> result = getMyHospitalsUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void execute_shouldCallRepositoryWithAuthenticatedUserId() {
        when(hospitalRepository.findHospitalsByUserId(anyLong())).thenReturn(List.of());

        getMyHospitalsUseCase.execute();

        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(hospitalRepository, times(1)).findHospitalsByUserId(userIdCaptor.capture());
        assertEquals(1L, userIdCaptor.getValue());
    }

    @Test
    public void execute_shouldThrowExceptionIfUserIsNotAuthenticated() {
        when(authUserContext.getCurrentUser()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> getMyHospitalsUseCase.execute());
    }
}
