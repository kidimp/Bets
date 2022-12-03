package org.chous.bets.models;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ExtraPoints {
    @DateTimeFormat(pattern = "dd.MM.yyy HH:mm")
    private Date dateAndTime;
    private int winningTeamId;
    private int secondPlaceTeamId;
    private int numberOfHitsOnTheCorrectScore;
    private int numberOfHitsOnTheMatchWinner;
    private double extraPoints;


    public ExtraPoints() {
    }


    public Date getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }


    public int getWinningTeamId() {
        return winningTeamId;
    }

    public void setWinningTeamId(int id) {
        this.winningTeamId = id;
    }


    public int getSecondPlaceTeamId() {
        return secondPlaceTeamId;
    }

    public void setSecondPlaceTeamId(int secondPlaceTeamId) {
        this.secondPlaceTeamId = secondPlaceTeamId;
    }


    public int getNumberOfHitsOnTheCorrectScore() {
        return numberOfHitsOnTheCorrectScore;
    }

    public void setNumberOfHitsOnTheCorrectScore(int numberOfHitsOnTheCorrectScore) {
        this.numberOfHitsOnTheCorrectScore = numberOfHitsOnTheCorrectScore;
    }


    public int getNumberOfHitsOnTheMatchWinner() {
        return numberOfHitsOnTheMatchWinner;
    }

    public void setNumberOfHitsOnTheMatchWinner(int numberOfHitsOnTheMatchWinner) {
        this.numberOfHitsOnTheMatchWinner = numberOfHitsOnTheMatchWinner;
    }


    public double getExtraPoints() {
        return extraPoints;
    }

    public void setExtraPoints(double extraPoints) {
        this.extraPoints = extraPoints;
    }
}
