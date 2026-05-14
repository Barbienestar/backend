package com.itesm.application.usecase;

import com.itesm.domain.models.Status;
import com.itesm.domain.repository.StatusRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

/** ListStatusUseCase */
@ApplicationScoped
public class ListStatusUseCase {

    private final StatusRepository statusRepository;

    @Inject
    public ListStatusUseCase(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public List<Status> execute() {
        return statusRepository.getAll();
    }
}
