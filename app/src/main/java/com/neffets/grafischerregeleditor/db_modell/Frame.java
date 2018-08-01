package com.neffets.grafischerregeleditor.db_modell;

public class Frame {
    private   long id;
    private  Rule rule;

    //Constructor
    public Frame() {
    }

    public Frame(Rule rule) {
        this.rule = rule;
    }

    public Frame(long id, Rule rule) {
        this.id = id;
        this.rule = rule;
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
}
