package org.chous.bets.models;

public class Bet {
    private int homeTeamScore;
    private int awayTeamScore;
    private boolean isExtraTime;
    private boolean isPenalty;

    public Bet(int homeTeamScore, int awayTeamScore, boolean isExtraTime, boolean isPenalty) {
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
        this.isExtraTime = isExtraTime;
        this.isPenalty = isPenalty;
    }

    public int getHomeTeamScore() {
        return homeTeamScore;
    }

    public void setHomeTeamScore(int homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public int getAwayTeamScore() {
        return awayTeamScore;
    }

    public void setAwayTeamScore(int awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }

    public boolean isExtraTime() {
        return isExtraTime;
    }

    public void setExtraTime(boolean extraTime) {
        isExtraTime = extraTime;
    }

    public boolean isPenalty() {
        return isPenalty;
    }

    public void setPenalty(boolean penalty) {
        isPenalty = penalty;
    }
}
