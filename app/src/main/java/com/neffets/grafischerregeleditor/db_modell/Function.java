package com.neffets.grafischerregeleditor.db_modell;

public class Function {
    private   long id;
    private  String name;
    private  Type type;
    private   String min_value;
    private   String max_value;

    //Constructor
    public Function() {
    }

    public Function(String name, Type type, String min_value, String max_value) {
        this.name = name;
        this.type = type;
        this.min_value = min_value;
        this.max_value = max_value;
    }

    public Function(long id, String name, Type type, String min_value, String max_value) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.min_value = min_value;
        this.max_value = max_value;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getMin_value() {
        return min_value;
    }

    public void setMin_value(String min_value) {
        this.min_value = min_value;
    }

    public String getMax_value() {
        return max_value;
    }

    public void setMax_value(String max_value) {
        this.max_value = max_value;
    }
}
