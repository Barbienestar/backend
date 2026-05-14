package com.itesm.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hospital {
    private Integer id;

    private String name;

    private String mapsUrl;

    public Hospital(Integer id) {
        this.id = id;
    }
}

