package org.chous.bets.services;

import org.chous.bets.models.Team;

import java.util.List;

public class TeamService {
        public static Team getTeamById(int teamId, List<Team> teams) {
        for (Team team : teams) {
            if (teamId == team.getId()) {
                return team;
            }
        }
        return null;
    }
}
