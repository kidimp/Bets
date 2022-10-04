package org.chous.bets.models;

import javax.validation.constraints.*;

public class Team {
    private int id;
    @NotEmpty(message = "Name must not be empty")
    @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters")
    private String name;
    @Min(value = 1, message = "Pot must be from 1 to 4")
    @Max(value = 4, message = "Pot must be from 1 to 4")
    private int pot;


    public Team(int id, String name, int pot) {
        this.id = id;
        this.name = name;
        this.pot = pot;
    }

    public Team() {

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


    public int getPot() {
        return pot;
    }

    public void setPot(int pot) {
        this.pot = pot;
    }
}
