package org.chous.bets.models;

import java.util.*;

public class TableRow {
    private final User user;
    private final List<BetView> betsViews = new ArrayList<>();
    private int position;
    private int roundIndex;
    private double extraPoints;


    public TableRow(User user, List<BetView> allBetViews, List<Match> matches, int roundIndex, double extraPoints) {
        this.user = user;
        this.roundIndex = roundIndex;
        this.extraPoints = extraPoints;
        for (BetView betView : allBetViews) {
            if (betView.getBet().getUserId() == user.getId()) {
                betsViews.add(betView);
            }
        }
        sortBetsByMatchDate(matches);
    }


    private void sortBetsByMatchDate(List<Match> matches) {
        for (int i = 0; i < matches.size(); i++) {
            boolean isFound = false;
            for (int j = 0; j < betsViews.size(); j++) {
                if (betsViews.get(j) != null && matches.get(i).getId() == betsViews.get(j).getBet().getMatchId()) {
                    if (i != j) {
                        betsViews.add(i, betsViews.get(j));
                        betsViews.remove(j + 1);
                    }
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                betsViews.add(i, null);
            }
        }
    }


    public List<BetView> getBetsViews() {
        return betsViews;
    }


    public User getUser() {
        return user;
    }


    public double getTotalPointsForThisRound() {
        double totalPoints = 0.0;
        for (BetView betView : betsViews) {
            if (betView != null) {
                totalPoints += betView.getBet().getPoints();
            }
        }
        if (roundIndex == 0) {
            totalPoints += extraPoints;
        }
        return totalPoints;
    }


    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

}
