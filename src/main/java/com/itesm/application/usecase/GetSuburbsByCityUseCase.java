package com.itesm.application.usecase;

import java.util.List;
import java.util.stream.Collectors;

import com.itesm.application.dto.SuburbDto;
import com.itesm.domain.repository.SuburbRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetSuburbsByCityUseCase {
    private final SuburbRepository suburbRepository;
    
    @Inject
    public GetSuburbsByCityUseCase(SuburbRepository suburbRepository) {
        this.suburbRepository = suburbRepository;
    }

    public List<SuburbDto> execute(Integer idCity) {
        return suburbRepository.getSuburbsByCity(idCity)
                .stream()
                .map(m -> new SuburbDto(
                    m.getId(),
                    m.getName(),
                    m.getZipCode()
                ))
                .collect(Collectors.toList());
    }
}
