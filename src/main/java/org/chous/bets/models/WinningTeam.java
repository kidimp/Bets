package org.chous.bets.models;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class WinningTeam {

    private int teamId;
    @DateTimeFormat(pattern = "dd.MM.yyy HH:mm")
    private Date dateAndTime;


    public WinningTeam() {
    }


    public int getWinningTeamId() {
        return teamId;
    }

    public void setWinningTeamId(int id) {
        this.teamId = id;
    }


    public Date getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }



}
