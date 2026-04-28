package com.itesm.application.dto;

public class StateDto {
    private Byte id;
    private String name;

    public StateDto(Byte id, String name) {
        this.id = id;
        this.name = name;
    }

    public StateDto() {
    }

    public void setId(Byte id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
}
