package com.neffets.grafischerregeleditor.db_modell;

public class Brick_Function {
    private  long id;
    private  Brick brick;
    private  Function function;
    private  String openhab_name;
    private  boolean trigger;
    private  boolean action;

    //Constructor
    public Brick_Function() {
    }

    public Brick_Function(Brick brick, String openhab_name, Function function, boolean trigger, boolean action) {
        this.brick = brick;
        this.openhab_name = openhab_name;
        this.function = function;
        this.trigger = trigger;
        this.action = action;
    }

    public Brick_Function(long id, Brick brick, String openhab_name, Function function, boolean trigger, boolean action) {
        this.id = id;
        this.brick = brick;
        this.function = function;
        this.openhab_name = openhab_name;
        this.trigger = trigger;
        this.action = action;
    }

    @Override
    public String toString() {
        return function.getName();
    }

    //Getter & Setter
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Brick getBrick() {
        return brick;
    }

    public void setBrick(Brick brick) {
        this.brick = brick;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public String getOpenhab_name() {
        return openhab_name;
    }

    public void setOpenhab_name(String openhab_name) {
        this.openhab_name = openhab_name;
    }

    public boolean isTrigger() {
        return trigger;
    }

    public void setTrigger(boolean trigger) {
        this.trigger = trigger;
    }

    public boolean isAction() {
        return action;
    }

    public void setAction(boolean action) {
        this.action = action;
    }
}
