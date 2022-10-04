package org.chous.bets.models;

public class Bet {
    private int playerId;
    private int matchId;
    private int homeTeamScore;
    private int awayTeamScore;
    private boolean isExtraTime;
    private boolean isPenalty;


    public Bet(int playerId, int matchId, int homeTeamScore, int awayTeamScore, boolean isExtraTime, boolean isPenalty) {
        this.playerId = playerId;
        this.matchId = matchId;
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
        this.isExtraTime = isExtraTime;
        this.isPenalty = isPenalty;
    }


    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }


    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
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

    public void setIsExtraTime(boolean isExtraTime) {
        this.isExtraTime = isExtraTime;
    }


    public boolean isPenalty() {
        return isPenalty;
    }

    public void setIsPenalty(boolean isPenalty) {
        this.isPenalty = isPenalty;
    }
}
