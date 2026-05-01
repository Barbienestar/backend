package com.itesm.domain.models;

public class State {
    private Byte id;
    private String name;

    public State(Byte id, String name) {
        this.id = id;
        this.name = name;
    }

    public State() {
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
