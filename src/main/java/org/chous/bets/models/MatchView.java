package org.chous.bets.models;

import java.util.List;

public class MatchView {
    private final Match match;
    private final List<Stage> stagesList;
    private final List<Team> teamList;

    public MatchView(Match match, List<Stage> stagesList, List<Team> teamsList) {
        this.match = match;
        this.stagesList = stagesList;
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


    public String getHomeTeam() {
        for (Team team : teamList) {
            if (team.getId() == match.getHomeTeamId()) {
                return team.getName();
            }
        }
        return "team not found";
    }


    public String getAwayTeam() {
        for (Team team : teamList) {
            if (team.getId() == match.getAwayTeamId()) {
                return team.getName();
            }
        }
        return "team not found";
    }
}
