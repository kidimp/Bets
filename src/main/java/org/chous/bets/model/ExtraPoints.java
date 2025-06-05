package org.chous.bets.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ExtraPoints {
//    @DateTimeFormat(pattern = "dd.MM.yyy HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date dateAndTime;
    private int winningTeamId;
    private int secondPlaceTeamId;
    private int numberOfHitsOnTheCorrectScore;
    private int numberOfHitsOnTheMatchResult;
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


    public int getNumberOfHitsOnTheMatchResult() {
        return numberOfHitsOnTheMatchResult;
    }

    public void setNumberOfHitsOnTheMatchResult(int numberOfHitsOnTheMatchResult) {
        this.numberOfHitsOnTheMatchResult = numberOfHitsOnTheMatchResult;
    }


    public double getExtraPoints() {
        return extraPoints;
    }

    public void setExtraPoints(double extraPoints) {
        this.extraPoints = extraPoints;
    }
}
