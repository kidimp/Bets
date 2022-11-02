package org.chous.bets.models;

import java.util.*;

public class UserBet {
    private final User user;
    private final List<BetView> betViews;
    private int totalPoints;


    public UserBet(User user, List<BetView> allBetViews, List<Match> matches) {
        this.user = user;

        betViews = new ArrayList<>();
        for (BetView betView : allBetViews) {
            if (betView.getBet().getUserId() == user.getId()) {
                betViews.add(betView);
            }
        }
        sortBetsByMatchDate(matches);
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


    public List<BetView> getBets() {
        return betViews;
    }


    public User getUser() {
        return user;
    }


    public int getTotalPoints() {
        return totalPoints;
    }
}
