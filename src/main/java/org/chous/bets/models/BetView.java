package org.chous.bets.models;

import org.chous.bets.services.PointsService;

import java.util.Date;
import java.util.List;

public class BetView {
    private final Bet bet;
    private Match match;
    private Team homeTeam;
    private Team awayTeam;
    private PointsService pointsService;
    private int round;

    public double points;

    public BetView(Bet bet, List<Match> matches, List<Team> teams) {
        this.bet = bet;

        for (Match match : matches) {
            if (bet.getMatchId() == match.getId()) {
                this.match = match;
                round = match.getRound();
            }
        }

        for (Team team : teams) {
            assert match != null;
            if (match.getHomeTeamId() == team.getId()) {
                this.homeTeam = team;
            }
            if (match.getAwayTeamId() == team.getId()) {
                this.awayTeam = team;
            }
        }

        pointsService = new PointsService(this.bet, match, homeTeam, awayTeam);
    }


    public BetView(Bet bet) {
        this.bet = bet;
    }


    public String getPoints() {
        String pointsInStr = String.valueOf(points);
        if (points % 1 == 0) {
            return pointsInStr.substring(0, pointsInStr.length() - 2);
        }
        return pointsInStr;
    }


    public void calculatePoints() {
        this.points = pointsService.getPointsForMatch();
    }


    public boolean isMatchStarted() {
        return !match.getDateAndTime().after(new Date());
    }


    public Bet getBet() {
        return bet;
    }


    public int getRound() {
        return round;
    }

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
