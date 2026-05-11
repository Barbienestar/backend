package com.itesm.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Role */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    private Byte id;
    private String name;

    public Role(Byte id) {
        this.id = id;
    }
}
