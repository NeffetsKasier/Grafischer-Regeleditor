package com.neffets.grafischerregeleditor.db_modell;

public class Operator {
    private  long id;
    private String name;
    private String character;

    //Constructor
    public Operator() {
    }

    public Operator(String name, String character) {
        this.name = name;
        this.character = character;
    }

    public Operator(long id, String name, String character) {
        this.id = id;
        this.name = name;
        this.character = character;
    }

    public String toString() {
        return getName();
    }

    //Getter & Setter
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }
}
