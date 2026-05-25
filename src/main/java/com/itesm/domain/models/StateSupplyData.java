package com.itesm.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StateSupplyData {
    private Byte stateId;
    private String stateName;
    private Double avgStock;
}
