package org.chous.bets.models;

import java.util.*;

public class PlayerBets {
    private User user;
    private List<BetView> bets;
    private int sum;

    public PlayerBets(User user, List<BetView> allBets, List<Match> matches) {
        this.user = user;

        bets = new ArrayList<>();
        for (BetView bet : allBets) {
            if (bet.getUserId() == user.getId()) {
                bets.add(bet);
            }
        }

        sortBetsByMathDate(matches);

        //TODO: Sorting by mathes
    }

    private void sortBetsByMathDate(List<Match> matches) {
        for (int i = 0; i < matches.size(); i++) {
            boolean isFound = false;
            for (int j = 0; j < bets.size(); j++) {
                if (bets.get(j) != null && matches.get(i).getId() == bets.get(j).getMatchId()) {
                    if (i != j) {
                        bets.add(i, bets.get(j));
                        bets.remove(j + 1);
                    }
                    isFound = true;
                    break;
                }
            }

            if (!isFound) {
                bets.add(i, null);
            }
        }
    }

    public List<BetView> getBets() { return bets; }

    public User getUser() { return user; }

    public int getSum() { return sum; }
}
