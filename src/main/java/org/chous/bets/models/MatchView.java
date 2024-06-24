package org.chous.bets.models;

import java.util.Date;
import java.util.List;

public class MatchView {
    private final Match match;
    private final List<Stage> stagesList;
    private final List<Round> roundList;
    private final List<Team> teamList;

    public MatchView(Match match, List<Stage> stagesList, List<Round> roundList, List<Team> teamsList) {
        this.match = match;
        this.stagesList = stagesList;
        this.roundList = roundList;
        this.teamList = teamsList;
    }


    public Match getMatch() {
        return match;
    }


    public String getStage() {
        for (Stage stage : stagesList) {
            if (stage.getId() == match.getStageId()) {
                return stage.getName();
            }
        }
        return "stage not found";
    }


    public String getRound() {
        for (Round round : roundList) {
            if (round.getRound() == match.getRound()) {
                return round.getName();
            }
        }
        return "round not found";
    }


    public Team getHomeTeam() {
        for (Team team : teamList) {
            if (team.getId() == match.getHomeTeamId()) {
                return team;
            }
        }
        return null;
    }


    public Team getAwayTeam() {
        for (Team team : teamList) {
            if (team.getId() == match.getAwayTeamId()) {
                return team;
            }
        }
        return null;
    }


    public String isExtraTime() {
        if (match.isExtraTime()) {
            return "extra";
        } else {
            return "";
        }
    }


    public String isPenalty() {
        if (match.isPenalty()) {
            return "pen";
        } else {
            return "";
        }
    }


    public boolean isMatchStarted() {
        return match.getDateAndTime().after(new Date());
    }

}
