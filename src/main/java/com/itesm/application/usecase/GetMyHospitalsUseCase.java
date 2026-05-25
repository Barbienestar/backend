package com.itesm.application.usecase;

import com.itesm.application.dto.HospitalDto;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.domain.models.Hospital;
import com.itesm.domain.repository.HospitalRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class GetMyHospitalsUseCase {

    private final HospitalRepository hospitalRepository;
    private final AuthenticatedUserContext authUserContext;

    @Inject
    public GetMyHospitalsUseCase(HospitalRepository hospitalRepository, AuthenticatedUserContext authUserContext) {
        this.hospitalRepository = hospitalRepository;
        this.authUserContext = authUserContext;
    }

    public List<HospitalDto> execute() {
        Long userId = authUserContext.getCurrentUser().getId();
        List<Hospital> hospitals = hospitalRepository.findHospitalsByUserId(userId);
        return hospitals.stream()
                .map(h -> new HospitalDto(h.getId(), h.getName()))
                .toList();
    }
}
