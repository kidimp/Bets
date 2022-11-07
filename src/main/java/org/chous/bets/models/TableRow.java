package org.chous.bets.models;

import org.chous.bets.services.MatchService;

import java.util.*;

public class TableRow {
    private final User user;
    private List<BetView> betViews;

    private int totalPoints;
    private int totalPointsFirstRound;
    private int totalPointsSecondRound;
    private int totalPointsThirdRound;
    private int totalPointsKnockoutRound;

    private List<BetView> userBetsViewsFirstRound;
    private List<BetView> userBetsViewsSecondRound;
    private List<BetView> userBetsViewsThirdRound;
    private List<BetView> userBetsViewsKnockoutRound;


    public TableRow(User user, List<BetView> allBetViews, List<Match> matches) {
        this.user = user;
        betViews = new ArrayList<>();
        for (BetView betView : allBetViews) {
            if (betView.getBet().getUserId() == user.getId()) {
                betViews.add(betView);
            }
        }
        sortBetsByMatchDate(matches);
    }


    public TableRow(User user, List<BetView> userBetsViewsFirstRound, List<BetView> userBetsViewsSecondRound,
                    List<BetView> userBetsViewsThirdRound, List<BetView> userBetsViewsKnockoutRound) {
        this.user = user;
        this.userBetsViewsFirstRound = userBetsViewsFirstRound;
        this.userBetsViewsSecondRound = userBetsViewsSecondRound;
        this.userBetsViewsThirdRound = userBetsViewsThirdRound;
        this.userBetsViewsKnockoutRound = userBetsViewsKnockoutRound;

    }


    private void sortBetsByMatchDate(List<Match> matches) {
        for (int i = 0; i < matches.size(); i++) {
            boolean isFound = false;
            for (int j = 0; j < betViews.size(); j++) {
                if (betViews.get(j) != null && matches.get(i).getId() == betViews.get(j).getBet().getMatchId()) {
                    if (i != j) {
                        betViews.add(i, betViews.get(j));
                        betViews.remove(j + 1);
                    }
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                betViews.add(i, null);
            }
        }
    }


    public List<BetView> getBetsViews() {
        return betViews;
    }


    public User getUser() {
        return user;
    }


    public double getTotalPointsForThisRound() {
        double totalPoints = 0.0;
        for (BetView betView : betViews) {
            if (betView != null) {
                totalPoints += betView.getPoints();
            }
        }
        return totalPoints;
    }


    public double getTotalPointsFirstRound() {
        for (BetView betView : userBetsViewsFirstRound) {
            if (betView != null) {
                betView.calculatePoints();
                totalPointsFirstRound += betView.getPoints();
            }
        }
        return totalPointsFirstRound;
    }


    public double getTotalPointsSecondRound() {
        for (BetView betView : userBetsViewsSecondRound) {
            if (betView != null) {
                betView.calculatePoints();
                totalPointsSecondRound += betView.getPoints();
            }
        }
        return totalPointsSecondRound;
    }


    public double getTotalPointsThirdRound() {
        for (BetView betView : userBetsViewsThirdRound) {
            if (betView != null) {
                betView.calculatePoints();
                totalPointsThirdRound += betView.getPoints();
            }
        }
        return totalPointsThirdRound;
    }


    public double getTotalPointsKnockoutRound() {
        for (BetView betView : userBetsViewsKnockoutRound) {
            if (betView != null) {
                betView.calculatePoints();
                totalPointsKnockoutRound += betView.getPoints();
            }
        }
        return totalPointsKnockoutRound;
    }


    public double getTotalPoints() {
        return totalPointsFirstRound + totalPointsSecondRound + totalPointsThirdRound + totalPointsKnockoutRound;
    }
}
