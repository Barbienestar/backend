package com.itesm.application.usecase;

import java.util.List;
import java.util.stream.Collectors;

import com.itesm.application.dto.StateDto;
import com.itesm.domain.repository.StateRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetAllStatesUseCase {
    private final StateRepository stateRepository;
    
    @Inject
    public GetAllStatesUseCase(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    public List<StateDto> execute() {
        return stateRepository.selectAll().stream()
            .map(s -> new StateDto(
                s.getId(),
                s.getName()
            ))
            .collect(Collectors.toList());
    }

}
