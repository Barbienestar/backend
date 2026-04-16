package com.itesm.application.usecase;

import com.itesm.application.dto.HospitalDto;
import com.itesm.domain.repository.HospitalRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetHospitalsUseCase {

    private final HospitalRepository hospitalRepository;

    @Inject
    public GetHospitalsUseCase(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public List<HospitalDto> execute() {
        return hospitalRepository.findAllHospitals().stream()
                .map(h -> new HospitalDto(h.getId(), h.getName()))
                .collect(Collectors.toList());
    }
}