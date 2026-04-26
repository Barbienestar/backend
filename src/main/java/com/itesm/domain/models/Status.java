package com.itesm.domain.models;

public class Status {
    private Byte id;
    private String name;

    public Status() {}
    public Status(Byte id, String name) {
        this.id = id;
        this.name = name;
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