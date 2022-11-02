package org.chous.bets.services;

import org.chous.bets.models.Bet;
import org.chous.bets.models.Match;

import java.util.List;

public class PointsService {
    Bet bet;
    List<Match> matchesList;

    public PointsService(Bet bet, List<Match> matchesList) {
        this.bet = bet;
        this.matchesList = matchesList;
    }


    public int getPointsForMatch() {
        for (Match match : matchesList) {
            if (bet.getMatchId() == match.getId()) {

                // Здагадванне поўнага рэзультата: 5 балаў
                if ((bet.getScoreHomeTeam() == match.getScoreHomeTeam()) &&
                        (bet.getScoreAwayTeam() == match.getScoreAwayTeam())) {
                    return 5;
                }
                // Памылка ў 1 гол ад поўнага рэзультата: 4 балы
                if (Math.abs((bet.getScoreHomeTeam() + bet.getScoreAwayTeam()) -
                        (match.getScoreHomeTeam() + match.getScoreAwayTeam())) == 1) {
                    return 4;
                }
                // Памылка ў 2 галы ад поўнага рэзультата: 3 балы
                if (Math.abs((bet.getScoreHomeTeam() + bet.getScoreAwayTeam()) -
                        (match.getScoreHomeTeam() + match.getScoreAwayTeam())) == 2) {
                    return 3;
                }
                // Правільны вынік гульні, але памылка больш, чым у 2 галы ад поўнага выніка: 2 балы
                if (((bet.getScoreHomeTeam() < bet.getScoreAwayTeam()) && (match.getScoreHomeTeam() < match.getScoreAwayTeam())) ||
                        ((bet.getScoreHomeTeam() > bet.getScoreAwayTeam()) && (match.getScoreHomeTeam() > match.getScoreAwayTeam()))) {
                    if (Math.abs((bet.getScoreHomeTeam() + bet.getScoreAwayTeam()) -
                            (match.getScoreHomeTeam() + match.getScoreAwayTeam())) >= 3) {
                        return 2;
                    }
                }
                // Неправільны вынік гульні, але здагаданае колькасць галоў адной з каманд: 1 бал
                if (((bet.getScoreHomeTeam() < bet.getScoreAwayTeam()) && (match.getScoreHomeTeam() > match.getScoreAwayTeam())) ||
                        ((bet.getScoreHomeTeam() > bet.getScoreAwayTeam()) && (match.getScoreHomeTeam() < match.getScoreAwayTeam()))) {
                    if ((bet.getScoreHomeTeam() == match.getScoreHomeTeam()) || (bet.getScoreAwayTeam() == match.getScoreAwayTeam())) {
                        return 1;
                    }
                }
                // Адваротны вынік: 1 бал
                if ((bet.getScoreHomeTeam() == match.getScoreAwayTeam()) &&
                        (bet.getScoreAwayTeam() == match.getScoreHomeTeam())) {
                    return 1;
                }

            }
        }
        return 0;
    }

}
