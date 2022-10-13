package org.chous.bets.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


public class Stage {
    private int id;
    @NotEmpty(message = "Name must not be empty")
    @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters")
    private String name;
    private boolean isKnockoutStage;


//    public Stage(int id, String name, boolean isKnockoutStage) {
//        this.id = id;
//        this.name = name;
//        this.isKnockoutStage = isKnockoutStage;
//    }


    public Stage() {
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

    public void setIsKnockoutStage(boolean isKnockoutStage) {
        this.isKnockoutStage = isKnockoutStage;
    }
}
