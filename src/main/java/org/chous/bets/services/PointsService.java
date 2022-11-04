package org.chous.bets.services;

import org.chous.bets.models.Bet;
import org.chous.bets.models.Match;

import java.util.List;

public class PointsService {
    private final Bet bet;
    private final List<Match> matchesList;
    private int points;

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
                    points = 5;
                }
                if (bet.getScoreHomeTeam() != bet.getScoreAwayTeam()) {
                    // Памылка ў 1 гол ад поўнага рэзультата: 4 балы
                    if (Math.abs((bet.getScoreHomeTeam() + bet.getScoreAwayTeam()) -
                            (match.getScoreHomeTeam() + match.getScoreAwayTeam())) == 1) {
                        points = 4;
                    }
                    // Памылка ў 2 галы ад поўнага рэзультата: 3 балы
                    if (Math.abs((bet.getScoreHomeTeam() + bet.getScoreAwayTeam()) -
                            (match.getScoreHomeTeam() + match.getScoreAwayTeam())) == 2) {
                        points = 3;
                    }
                    // Правільны вынік гульні, але памылка больш, чым у 2 галы ад поўнага выніка: 2 балы
                    if (((bet.getScoreHomeTeam() < bet.getScoreAwayTeam()) && (match.getScoreHomeTeam() < match.getScoreAwayTeam())) ||
                            ((bet.getScoreHomeTeam() > bet.getScoreAwayTeam()) && (match.getScoreHomeTeam() > match.getScoreAwayTeam()))) {
                        if (Math.abs((bet.getScoreHomeTeam() + bet.getScoreAwayTeam()) -
                                (match.getScoreHomeTeam() + match.getScoreAwayTeam())) >= 3) {
                            points = 2;
                        }
                    }
                    // Неправільны вынік гульні, але здагаданае колькасць галоў адной з каманд: 1 бал
                    if (((bet.getScoreHomeTeam() < bet.getScoreAwayTeam()) && (match.getScoreHomeTeam() > match.getScoreAwayTeam())) ||
                            ((bet.getScoreHomeTeam() > bet.getScoreAwayTeam()) && (match.getScoreHomeTeam() < match.getScoreAwayTeam()))) {
                        if ((bet.getScoreHomeTeam() == match.getScoreHomeTeam()) || (bet.getScoreAwayTeam() == match.getScoreAwayTeam())) {
                            points = 1;
                        }
                    }
                    // Адваротны вынік: 1 бал
                    if ((bet.getScoreHomeTeam() == match.getScoreAwayTeam()) &&
                            (bet.getScoreAwayTeam() == match.getScoreHomeTeam())) {
                        points = 1;
                    }
                } else {
                    // У выпадку пастаўленнай нічыі розніца галоў скарачаецца ў 2 разы.
                    // Памылка ў 1 гол ад поўнага рэзультата: 4 балы
                    if (Math.abs((bet.getScoreHomeTeam() + bet.getScoreAwayTeam()) -
                            (match.getScoreHomeTeam() + match.getScoreAwayTeam())) == 2) {
                        points = 4;
                    }
                    // У выпадку пастаўленнай нічыі розніца галоў скарачаецца ў 2 разы.
                    // Памылка ў 2 галы ад поўнага рэзультата: 3 балы
                    if (Math.abs((bet.getScoreHomeTeam() + bet.getScoreAwayTeam()) -
                            (match.getScoreHomeTeam() + match.getScoreAwayTeam())) == 4) {
                        points = 3;
                    }
                    // У выпадку пастаўленнай нічыі розніца галоў скарачаецца ў 2 разы.
                    // Правільны вынік гульні, але памылка больш, чым у 2 галы ад поўнага выніка: 2 балы
                    if (Math.abs((bet.getScoreHomeTeam() + bet.getScoreAwayTeam()) -
                            (match.getScoreHomeTeam() + match.getScoreAwayTeam())) >= 6) {
                        points = 2;
                    }
                }

                // У фінальнай стадыі на гульню можна ставіць з улікам дадатковага часу (+) і пенальці (++).
                // За здагаданны першы дадаецца 5 балаў, за здагаданны другі - 10.
                // У выпадку пастаўленнага пенальці ці дадатковага часу, калі адпаведная падзея не адбылася,
                // колькасць балаў максімальных балаў скарачаецца на 2 і 3 адпаведна (але не можа быць менш за 0).
                if (bet.isExtraTime() == match.isExtraTime()) {
                    points += 5;
                }

                if (bet.isPenalty() == match.isPenalty()) {
                    points += 10;
                }

                if ((bet.isExtraTime()) && !(match.isExtraTime())) {
                    points -= 2;
                }

                if ((bet.isPenalty()) && !(match.isPenalty())) {
                    points -= 3;
                }



            }
        }
        return points;
    }

}
