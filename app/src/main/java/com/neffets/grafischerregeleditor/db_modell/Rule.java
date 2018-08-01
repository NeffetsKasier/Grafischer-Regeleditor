package com.neffets.grafischerregeleditor.db_modell;

import java.io.Serializable;

public class Rule implements Serializable {
    private  long id;
    private  String name;
    private  Service service;
    private  boolean active;

    //Constructor
    public Rule() {
    }

    public Rule(String name, Service service) {
        this.name = name;
        this.service = service;
        this.active = false;
    }

    public Rule(long id, String name, Service service, boolean active) {
        this.id = id;
        this.name = name;
        this.service = service;
        this.active = active;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public String toString() {
        return getName();
    }
}
