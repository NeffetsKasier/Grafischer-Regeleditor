package com.neffets.grafischerregeleditor.db_modell;

import java.io.Serializable;

public class Service implements Serializable{
    private  long id;
    private  String name;

    //Constructor
    public Service() {
    }

    public Service(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Service(String name) {
        this.name = name;
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

    public String toString() {
        return getName();
    }
}
