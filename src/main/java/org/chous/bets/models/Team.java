package org.chous.bets.models;

public class Team {
    private int id;
    private String name;
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
