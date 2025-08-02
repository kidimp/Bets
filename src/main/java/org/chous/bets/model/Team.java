package org.chous.bets.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Comparator;


public class Team implements Comparable<Team> {
    private int id;
    @NotEmpty(message = "Name must not be empty")
    @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters")
    private String name;
    @NotEmpty(message = "Name must not be empty")
    @Size(min = 2, max = 2, message = "Name must be between 2 characters")
    private String isoName;

    @Min(value = 1, message = "Pot must be from 1 to 4")
    @Max(value = 4, message = "Pot must be from 1 to 4")
    private int pot;


    public Team() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsoName() {
        return isoName;
    }

    public void setIsoName(String isoName) {
        this.isoName = isoName;
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


    @Override
    public int compareTo(Team o) {
        return Comparators.NAME.compare(this, o);
    }


    public static class Comparators {
        public static Comparator<Team> NAME = new Comparator<Team>() {
            @Override
            public int compare(Team o1, Team o2) {
                return o1.name.compareTo(o2.name);
            }
        };
    }

}
