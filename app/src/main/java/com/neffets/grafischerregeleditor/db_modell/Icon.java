package com.neffets.grafischerregeleditor.db_modell;

public class Icon {
    private  long id;
    private  String filename;
    private   String name;

    //Constructor
    public Icon() {
    }

    public Icon(String filename, String name) {
        this.filename = filename;
        this.name = name;
    }

    public Icon(long id, String filename, String name) {
        this.id = id;
        this.filename = filename;
        this.name = name;
    }


    //Getter & Setter
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
