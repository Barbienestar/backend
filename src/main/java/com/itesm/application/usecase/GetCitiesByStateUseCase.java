package com.itesm.application.usecase;

import java.util.List;
import java.util.stream.Collectors;

import com.itesm.application.dto.CityDto;
import com.itesm.domain.repository.CityRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetCitiesByStateUseCase {
    private final CityRepository cityRepository;

    @Inject
    public GetCitiesByStateUseCase(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<CityDto> execute(Byte idState) {
        return cityRepository.getCitiesByState(idState)
                .stream()
                .map(m -> new CityDto(
                    m.getId(),
                    m.getName()
                ))
                .collect(Collectors.toList());
    }
}
