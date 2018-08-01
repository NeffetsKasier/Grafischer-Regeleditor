package com.neffets.grafischerregeleditor.db_modell;

public class Precondition {
    private  long id;
    private  Brick_Function brick_function;
    private  Frame frame;
    private  Operator operator;
    private  String value;

    //Constructor
    public Precondition() {
    }

    public Precondition(Brick_Function brick_function, Frame frame, Operator operator, String value) {
        this.brick_function = brick_function;
        this.frame = frame;
        this.operator = operator;
        this.value = value;
    }

    public Precondition(int id, Brick_Function brick_function, Frame frame, Operator operator, String value) {
        this.id = id;
        this.brick_function = brick_function;
        this.frame = frame;
        this.operator = operator;
        this.value = value;
    }

    //Getter & Setter
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Brick_Function getBrick_function() {
        return brick_function;
    }

    public void setBrick_function(Brick_Function brick_function) {
        this.brick_function = brick_function;
    }

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
