package org.chous.bets.models;

import java.util.*;

public class TableRow {
    private final User user;
    private final List<BetView> betsViews = new ArrayList<>();

    private int position;

    private double totalPointsFirstRound;
    private double totalPointsSecondRound;
    private double totalPointsThirdRound;
    private double totalPointsKnockoutRound;
    private double totalPointsWholeTournament;

//    private List<BetView> userBetsViewsFirstRound;
//    private List<BetView> userBetsViewsSecondRound;
//    private List<BetView> userBetsViewsThirdRound;
//    private List<BetView> userBetsViewsKnockoutRound;
//
//    private final TreeMap<Double, User> totalPointsForEachUserFirstRound = new TreeMap<>();
//    private final TreeMap<Double, User> totalPointsForEachUserSecondRound = new TreeMap<>();
//    private final TreeMap<Double, User> totalPointsForEachUserThirdRound = new TreeMap<>();
//    private final TreeMap<Double, User> totalPointsForEachUserKnockoutRound = new TreeMap<>();
//    private final TreeMap<Double, User> totalPointsForEachUserWholeTournament = new TreeMap<>();

    private double averagePosition;


    public TableRow(User user, List<BetView> allBetViews, List<Match> matches) {
        this.user = user;
        for (BetView betView : allBetViews) {
            if (betView.getBet().getUserId() == user.getId()) {
                betsViews.add(betView);
            }
        }
        sortBetsByMatchDate(matches);
    }


    public TableRow(User user, double totalPointsFirstRound , double totalPointsSecondRound,
                    double totalPointsThirdRound, double totalPointsKnockoutRound, double totalPointsWholeTournament) {
        this.user = user;
        this.totalPointsFirstRound = totalPointsFirstRound;
        this.totalPointsSecondRound = totalPointsSecondRound;
        this.totalPointsThirdRound = totalPointsThirdRound;
        this.totalPointsKnockoutRound = totalPointsKnockoutRound;
        this.totalPointsWholeTournament = totalPointsWholeTournament;

//        totalPointsForEachUserFirstRound.put(totalPointsFirstRound, user);
//        totalPointsForEachUserSecondRound.put(totalPointsSecondRound, user);
//        totalPointsForEachUserThirdRound.put(totalPointsThirdRound, user);
//        totalPointsForEachUserKnockoutRound.put(totalPointsKnockoutRound, user);
//        totalPointsForEachUserWholeTournament.put(totalPointsWholeTournament, user);
    }

    public TableRow(User user, double averagePosition, double totalPointsFirstRound , double totalPointsSecondRound,
                    double totalPointsThirdRound, double totalPointsKnockoutRound, double totalPointsWholeTournament) {
        this.user = user;
        this.averagePosition = averagePosition;
        this.totalPointsFirstRound = totalPointsFirstRound;
        this.totalPointsSecondRound = totalPointsSecondRound;
        this.totalPointsThirdRound = totalPointsThirdRound;
        this.totalPointsKnockoutRound = totalPointsKnockoutRound;
        this.totalPointsWholeTournament = totalPointsWholeTournament;
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
        return totalPoints;
    }


    public double getTotalPointsFirstRound() {
        return totalPointsFirstRound;
    }


    public double getTotalPointsSecondRound() {
        return totalPointsSecondRound;
    }


    public double getTotalPointsThirdRound() {
        return totalPointsThirdRound;
    }


    public double getTotalPointsKnockoutRound() {
        return totalPointsKnockoutRound;
    }


    public double getTotalPointsWholeTournament() {
        return totalPointsWholeTournament;
    }


    public double getAveragePosition() {
        return averagePosition;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }


    //    public double getTotalPointsFirstRound() {
//        return totalPointsFirstRound;
//    }
//
//    public void setTotalPointsFirstRound() {
//        for (BetView betView : userBetsViewsFirstRound) {
//            if (betView != null) {
////                betView.calculatePoints();
//                totalPointsFirstRound += betView.getBet().getPoints();
//            }
//        }
//        //totalPointsForEachUserFirstRound.put(totalPointsFirstRound, user);
//    }
//
//
//    public double getTotalPointsSecondRound() {
//        return totalPointsSecondRound;
//    }
//
//    public void setTotalPointsSecondRound() {
//        for (BetView betView : userBetsViewsSecondRound) {
//            if (betView != null) {
////                betView.calculatePoints();
//                totalPointsSecondRound += betView.getBet().getPoints();
//            }
//        }
////        totalPointsForEachUserSecondRound.put(totalPointsSecondRound, user);
//    }
//
//
//    public double getTotalPointsThirdRound() {
//        return totalPointsThirdRound;
//    }
//
//    public void setTotalPointsThirdRound() {
//        for (BetView betView : userBetsViewsThirdRound) {
//            if (betView != null) {
////                betView.calculatePoints();
//                totalPointsThirdRound += betView.getBet().getPoints();
//            }
//        }
////        totalPointsForEachUserThirdRound.put(totalPointsThirdRound, user);
//    }
//
//
//    public double getTotalPointsKnockoutRound() {
//        return totalPointsKnockoutRound;
//    }
//
//    public void setTotalPointsKnockoutRound() {
//        for (BetView betView : userBetsViewsKnockoutRound) {
//            if (betView != null) {
////                betView.calculatePoints();
//                totalPointsKnockoutRound += betView.getBet().getPoints();
//            }
//        }
////        totalPointsForEachUserKnockoutRound.put(totalPointsKnockoutRound, user);
//    }
//
//
//    public double getTotalPointsWholeTournament() {
//        return totalPointsWholeTournament;
//    }
//
//    public void setTotalPointsWholeTournament() {
//        totalPointsWholeTournament = totalPointsFirstRound + totalPointsSecondRound +
//                totalPointsThirdRound + totalPointsKnockoutRound;
////        totalPointsForEachUserWholeTournament.put(totalPointsWholeTournament, user);
//    }
//
//
//
//    public int getAveragePositionByRound(TreeMap<Double, User> totalPointsForEachUserByRound) {
//        int position = 0;
//        for (Map.Entry entry : totalPointsForEachUserByRound.entrySet()) {
//            position++;
//            if (entry.getValue() == user) {
//                return position;
//            }
//        }
//        return 0;
//    }
//
//    public double getAveragePosition() {
//        return averagePosition;
//    }
//
//    public void setAveragePosition() {
//        averagePosition = (getAveragePositionByRound(totalPointsForEachUserFirstRound) +
//                getAveragePositionByRound(totalPointsForEachUserSecondRound) +
//                getAveragePositionByRound(totalPointsForEachUserThirdRound) +
//                getAveragePositionByRound(totalPointsForEachUserKnockoutRound) +
//                getAveragePositionByRound(totalPointsForEachUserWholeTournament)) / 5.0;
//    }

}
