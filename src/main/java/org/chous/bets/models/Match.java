package org.chous.bets.models;

import java.util.Date;

public class Match {
    private int id;
    private int homeTeamId;
    private int AwayTeamId;
    private Date date;
    private String Score;
    private String Stage;

    public Match(int id, int homeTeamId, int awayTeamId, Date date, String score, String stage) {
        this.id = id;
        this.homeTeamId = homeTeamId;
        AwayTeamId = awayTeamId;
        this.date = date;
        Score = score;
        Stage = stage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(int homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public int getAwayTeamId() {
        return AwayTeamId;
    }

    public void setAwayTeamId(int awayTeamId) {
        AwayTeamId = awayTeamId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public String getStage() {
        return Stage;
    }

    public void setStage(String stage) {
        Stage = stage;
    }
}