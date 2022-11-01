package org.chous.bets.models;

import java.util.Date;
import java.util.List;

public class BetView {
    private final Bet bet;

    private List<Match> matches;

    public BetView(Bet bet, List<Match> matches) {
        this.bet = bet;
        this.matches = matches;
    }

    public BetView(Bet bet) {
        this.bet = bet;
    }


    public boolean isMatchStarted() {
        for (Match match : matches) {
            if (bet.getMatchId() == match.getId()) {
                if (match.getDateAndTime().after(new Date())) {
                    return false;
                }
            }
        }
        return true;
    }


    public Bet getBet() {
        return bet;
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
