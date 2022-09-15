package org.chous.bets.models;

import javax.validation.constraints.*;

public class Team {
    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 3, max = 30, message = "Name should be between 3 and 30 characters")
    private String name;

    @Min(value = 1, message = "Basket should be from 1 to 4")
    @Max(value = 4, message = "Basket should be from 1 to 4")
    private int basket;

    public Team(int id, String name, int basket) {
        this.id = id;
        this.name = name;
        this.basket = basket;
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

    public int getBasket() {
        return basket;
    }

    public void setBasket(int basket) {
        this.basket = basket;
    }
}
