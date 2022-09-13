package org.chous.bets.models;

public class Stage {
    private int id;
    private String name;
    private boolean isKnockoutStage;

    public Stage(int id, String name, boolean isKnockoutStage) {
        this.id = id;
        this.name = name;
        this.isKnockoutStage = isKnockoutStage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isKnockoutStage() {
        return isKnockoutStage;
    }

    public void setKnockoutStage(boolean knockoutStage) {
        isKnockoutStage = knockoutStage;
    }
}
