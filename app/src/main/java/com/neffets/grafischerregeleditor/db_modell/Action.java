package com.neffets.grafischerregeleditor.db_modell;

public class Action {
    private long id;
    private Rule rule;
    private  Brick_Function brick_function;
    private  String value;

    //Constructor
    public Action() {
    }

    public Action(Rule rule, Brick_Function brick_function, String value) {
        this.rule = rule;
        this.brick_function = brick_function;
        this.value = value;
    }

    public Action(long id, Rule rule, Brick_Function brick_function, String value) {
        this.id = id;
        this.rule = rule;
        this.brick_function = brick_function;
        this.value = value;
    }

    //Getter & Setter
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public Brick_Function getBrick_function() {
        return brick_function;
    }

    public void setBrick_function(Brick_Function brick_function) {
        this.brick_function = brick_function;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
