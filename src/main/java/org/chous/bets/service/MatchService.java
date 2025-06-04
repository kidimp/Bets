package org.chous.bets.service;

import org.chous.bets.model.Match;

import java.util.ArrayList;
import java.util.List;

public class MatchService {
    public static Match getMatchById(int matchId, List<Match> matches) {
        for (Match match : matches) {
            if (matchId == match.getId()) {
                return match;
            }
        }
        return null;
    }


    public static List<Match> getMatchesByRound(int round, List<Match> matches) {
        List<Match> matchesByRound = new ArrayList<>();
        for (Match match : matches) {
            if (match.getRound() == round) {
                matchesByRound.add(match);
            }
        }
        return matchesByRound;
    }


}
