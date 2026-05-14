package com.itesm.application.usecase;

import com.itesm.application.dto.MedicineHospitalStockDto;
import com.itesm.domain.models.MedicineHospitalStock;
import com.itesm.domain.repository.MedicineHospitalStockRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetStockByMedicineUseCase {

    private final MedicineHospitalStockRepository repository;

    @Inject
    public GetStockByMedicineUseCase(MedicineHospitalStockRepository repository) {
        this.repository = repository;
    }

    public List<MedicineHospitalStockDto> execute(String medicineName) {
        return repository.findByMedicineName(medicineName).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private MedicineHospitalStockDto toDto(MedicineHospitalStock item) {
        return new MedicineHospitalStockDto(
                item.getHospitalId(),
                item.getHospitalName(),
                item.getAddress(),
                resolveStockLabel(item.getStock()),
                resolveStatus(item.getStock()),
                item.getMapsUrl()
        );
    }

    private String resolveStockLabel(int stock) {
        if (stock == 0)  return "No disponible";
        if (stock <= 10) return stock + " piezas restantes";
        return "Stock Alto";
    }

    private String resolveStatus(int stock) {
        if (stock == 0)  return "Agotado";
        if (stock <= 10) return "Limitado";
        return "Disponible";
    }
}