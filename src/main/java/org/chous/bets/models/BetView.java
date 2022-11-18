package org.chous.bets.models;

import org.chous.bets.services.PointsService;

import java.util.Date;

public class BetView {
    private final Bet bet;
    private Match match;
    private Team homeTeam;
    private Team awayTeam;
    private PointsService pointsService;
//    private int round;
//    private double points;


    public BetView(Bet bet, Match match, Team homeTeam, Team awayTeam) {
        this.bet = bet;
        this.match = match;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        pointsService = new PointsService(this.bet, match, homeTeam, awayTeam);
    }


    public BetView(Bet bet) {
        this.bet = bet;
    }


//    public double getPoints() {
//        return points;
//    }


//    public String getPointsInStr() {
//        String pointsInStr = String.valueOf(points);
//        if (points % 1 == 0) {
//            return pointsInStr.substring(0, pointsInStr.length() - 2);
//        }
//        return pointsInStr;
//    }


    public void calculatePoints() {
        bet.setPoints(pointsService.getPointsForMatch());
//        this.points = pointsService.getPointsForMatch();
    }


    public boolean isMatchStarted() {
        return !match.getDateAndTime().after(new Date());
    }


    public Bet getBet() {
        return bet;
    }


//    public int getRound() {
//        return round;
//    }


    public String isExtraTime() {
        if (bet.isExtraTime()) {
            return "extra";
        } else {
            return "";
        }
    }


    public String isPenalty() {
        if (bet.isPenalty()) {
            return "pen";
        } else {
            return "";
        }
    }

}
