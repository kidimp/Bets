package org.chous.bets.models;

import java.util.ArrayList;
import java.util.List;

public class TableView {
    private final User user;
    private final List<Match> matchesList;
    private final List<BetView> betsViewsList;
    private final List<String> rowContent;

    public TableView(User user, List<Match> matchesList, List<BetView> betsViewsList) {
        this.user = user;
        this.matchesList = matchesList;
        this.betsViewsList = betsViewsList;

        int dif = matchesList.size() - betsViewsList.size();
        for (int i = 0; i < dif; i++) {
            betsViewsList.add(null);
        }

        for (int i = 0; i < matchesList.size(); i++) {
            for (int j = 0; j < betsViewsList.size(); j++) {
                if (betsViewsList.get(j) != null && matchesList.get(i).getId() == betsViewsList.get(j).getMatchId()) {
                    betsViewsList.add(i, betsViewsList.get(j));
                    betsViewsList.remove(j + 1);
                    break;
                }
            }
        }

        rowContent = new ArrayList<>();
        setRowContent();
    }

    public void setRowContent() {
        rowContent.add(user.getUsername());
        rowContent.add("total points");


        for (BetView betView : betsViewsList) {

            if (betView != null) {
                rowContent.add(betView.getScoreHomeTeam() + " : " + betView.getScoreAwayTeam() + " " + betView.isExtraTime() + " " + betView.isPenalty());
                rowContent.add("???");
            } else {
                rowContent.add("no bet");
                rowContent.add("???");
            }

        }

        rowContent.add("total points");
    }

    public List<String> getRowContent() {
        return rowContent;
    }
}
