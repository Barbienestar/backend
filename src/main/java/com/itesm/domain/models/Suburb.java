package com.itesm.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Suburb {
    private Integer id;
    private String name;
    private String zipCode;
    private Integer idState;
}
