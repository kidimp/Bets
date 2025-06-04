package org.chous.bets.model;


public class Round {
    private int id;
    private String name;
    private int round;


    public Round() {
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


    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }
}
