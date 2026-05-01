package com.itesm.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Address */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String address;
    private Integer suburbId;

    public Address(Integer suburbId) {
        this.suburbId = suburbId;
    }
}
