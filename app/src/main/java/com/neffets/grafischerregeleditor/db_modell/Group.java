package com.neffets.grafischerregeleditor.db_modell;

public class Group {
    private long id;
    private   String name;

    //Constructor
    public Group() {
    }

    public Group(String name) {
        this.name = name;

    }

    public Group(long id, String name) {
        this.id = id;
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

    @Override
    public String toString() {
        return getName();
    }
}
