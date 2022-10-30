package org.chous.bets.models;

import java.util.List;

public class BetView {
    private final Bet bet;

    public BetView(Bet bet) {
        this.bet = bet;
    }

    public int getId() {
        return bet.getId();
    }

    public int getMatchId() {
        return bet.getMatchId();
    }

    public int getUserId() {
        return bet.getUserId();
    }

    public int getScoreHomeTeam() {
        return bet.getScoreHomeTeam();
    }

    public int getScoreAwayTeam() {
        return bet.getScoreAwayTeam();
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
