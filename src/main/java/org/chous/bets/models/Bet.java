package org.chous.bets.models;

import javax.validation.constraints.Min;

public class Bet {
    private int id;
    private int userId;
    private int matchId;
    @Min(value = 0, message = "Значение не может быть отрицательным")
//    @NotEmpty(message = "Value cannot be empty")
    private int scoreHomeTeam;
    @Min(value = 0, message = "Значение не может быть отрицательным")
//    @NotBlank(message = "Value cannot be empty")
    private int scoreAwayTeam;
    private boolean isExtraTime;
    private boolean isPenalty;
    private double points;

    public Bet() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }


    public int getScoreHomeTeam() {
        return scoreHomeTeam;
    }

    public void setScoreHomeTeam(int scoreHomeTeam) {
        this.scoreHomeTeam = scoreHomeTeam;
    }


    public int getScoreAwayTeam() {
        return scoreAwayTeam;
    }

    public void setScoreAwayTeam(int scoreAwayTeam) {
        this.scoreAwayTeam = scoreAwayTeam;
    }


    public boolean isExtraTime() {
        return isExtraTime;
    }

    public void setIsExtraTime(boolean isExtraTime) {
        this.isExtraTime = isExtraTime;
    }


    public boolean isPenalty() {
        return isPenalty;
    }

    public void setIsPenalty(boolean isPenalty) {
        this.isPenalty = isPenalty;
    }


    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }
}
