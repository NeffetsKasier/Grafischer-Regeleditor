package com.neffets.grafischerregeleditor.db_modell;

public class Brick {
    private  long id;
    private String name;
    private Group group;
    private  Icon icon;

    //Constructor
    public Brick() {
    }

    public Brick(String name, Group group, Icon icon) {
        this.name = name;
        this.group = group;
        this.icon = icon;
    }

    public Brick(long id, String name, Group group, Icon icon) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.icon = icon;
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }
}

