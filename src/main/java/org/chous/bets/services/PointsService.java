package org.chous.bets.services;

import org.chous.bets.models.Bet;
import org.chous.bets.models.Match;
import org.chous.bets.models.Team;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PointsService {
    private Bet bet;
    private Match match;
    private Team homeTeam;
    private Team awayTeam;
    private double points;
    private int winningTeamId;
    private int secondTeamId;
    private int winningTeamIdByUser;
    private double extraPoints;


    public PointsService(Bet bet, Match match, Team homeTeam, Team awayTeam) {
        this.bet = bet;
        this.match = match;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }


    public PointsService(int winningTeamId, int secondTeamId, int winningTeamIdByUser) {
        this.winningTeamId = winningTeamId;
        this.secondTeamId = secondTeamId;
        this.winningTeamIdByUser = winningTeamIdByUser;
    }


    public double getPointsForMatch() {
        if (bet.getMatchId() == match.getId()) {

            // Здагадванне поўнага рэзультата: 5 балаў
            if ((bet.getScoreHomeTeam() == match.getScoreHomeTeam()) &&
                    (bet.getScoreAwayTeam() == match.getScoreAwayTeam())) {
                points = 5;
            }
            // У матчы ёсць пераможца (няма нічыі)
            if (match.getScoreHomeTeam() != match.getScoreAwayTeam()) {
                // Гулец угадаў вынік матчу
                if (isHitOnTheMatchResult()) {
                    // Памылка ў 1 гол ад поўнага рэзультата: 4 балы
                    if ((Math.abs((bet.getScoreHomeTeam() - match.getScoreHomeTeam()))) +
                            (Math.abs((bet.getScoreAwayTeam() - match.getScoreAwayTeam()))) == 1) {
                        points = 4;
                    }
                    // Памылка ў 2 галы ад поўнага рэзультата: 3 балы
                    if ((Math.abs((bet.getScoreHomeTeam() - match.getScoreHomeTeam()))) +
                            (Math.abs((bet.getScoreAwayTeam() - match.getScoreAwayTeam()))) == 2) {
                        points = 3;
                    }
                    // Правільны вынік гульні, але памылка больш, чым у 2 галы ад поўнага выніка: 2 балы
                    if ((Math.abs((bet.getScoreHomeTeam() - match.getScoreHomeTeam()))) +
                            (Math.abs((bet.getScoreAwayTeam() - match.getScoreAwayTeam()))) >= 3) {
                        points = 2;
                    }
                }

            } else {
                if (bet.getScoreHomeTeam() == bet.getScoreAwayTeam()) {
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
                /*if (!isHitOnTheMatchResult()) {
                    // Неправільны вынік гульні, але здагаданае колькасць галоў адной з каманд: 1 бал
                    if ((bet.getScoreHomeTeam() == match.getScoreHomeTeam()) || (bet.getScoreAwayTeam() == match.getScoreAwayTeam())) {
                        points = 1;
                    }
                }*/
            }

            if (!isHitOnTheMatchResult()) {
                // Неправільны вынік гульні, але здагаданае колькасць галоў адной з каманд: 1 бал
                if ((bet.getScoreHomeTeam() == match.getScoreHomeTeam()) || (bet.getScoreAwayTeam() == match.getScoreAwayTeam())) {
                    points = 1;
                }
                // Адваротны вынік: 1 бал
                if ((bet.getScoreHomeTeam() == match.getScoreAwayTeam()) &&
                        (bet.getScoreAwayTeam() == match.getScoreHomeTeam())) {
                    points = 1;
                }
            }

            // У фінальнай стадыі на гульню можна ставіць з улікам дадатковага часу (+) і пенальці (++).
            // За здагаданны першы дадаецца 5 балаў, за здагаданны другі - 10.
            // У выпадку пастаўленнага пенальці ці дадатковага часу, калі адпаведная падзея не адбылася,
            // колькасць балаў максімальных балаў скарачаецца на 2 і 3 адпаведна (але не можа быць менш за 0).
            if ((match.isExtraTime()) && (bet.isExtraTime() == match.isExtraTime())) {
                points += 5;
            }
            if ((match.isPenalty()) && (bet.isPenalty() == match.isPenalty())) {
                points += 5;
            }
            if (!(match.isExtraTime()) && (bet.isExtraTime())) {
                points -= 2;
            }
            if (!(match.isPenalty()) && (bet.isPenalty())) {
                points -= 3;
            }

            // Да кожнай стаўцы прымяняецца множнік, які вылічваецца з пазіцый каманд у рэйтынгу УЕФА
            // (ад 4 да 1). Калі перамагла каманда з ніжэйшым узроўнем, то агульная колькасць ачкоў
            // памнажаецца на (1.3, 1.5, 2 - адпаведна розніцы ў рэйтынгу ў 1, 2, 3), калі стаўка зробленая на
            // гэтую каманду. У выпадку адгаданай нічыі рэзультат памнажаецца на 1.3
            if (isHitOnTheMatchResult()) {
                if (homeTeam.getPot() != awayTeam.getPot()) {
                    if (((match.getScoreHomeTeam() > match.getScoreAwayTeam()) && (homeTeam.getPot() > awayTeam.getPot())) ||
                            ((match.getScoreAwayTeam() > match.getScoreHomeTeam()) && (awayTeam.getPot() > homeTeam.getPot()))) {
                        switch (Math.abs(homeTeam.getPot() - awayTeam.getPot())) {
                            case 1:
                                points *= 1.3;
                                break;
                            case 2:
                                points *= 1.5;
                                break;
                            case 3:
                                points *= 2;
                                break;
                        }
                    }
                    if (match.getScoreHomeTeam() == match.getScoreAwayTeam()) {
                        points *= 1.3;
                    }
                }
            }

        }

        if (points < 0) {
            points = 0;
        }

        return points;
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public boolean isHitOnTheCorrectScore() {
        // Здагадванне поўнага рэзультата
        return (bet.getScoreHomeTeam() == match.getScoreHomeTeam()) &&
                (bet.getScoreAwayTeam() == match.getScoreAwayTeam());
    }


    public boolean isHitOnTheMatchResult() {
        // Здагадванне рэзультата матчу
        if (((bet.getScoreHomeTeam() - bet.getScoreAwayTeam()) < 0) &&
                ((match.getScoreHomeTeam() - match.getScoreAwayTeam()) < 0)) {
            return true;
        }
        if (((bet.getScoreAwayTeam() - bet.getScoreHomeTeam()) < 0) &&
                ((match.getScoreAwayTeam() - match.getScoreHomeTeam()) < 0)) {
            return true;
        }
        return (bet.getScoreAwayTeam() == bet.getScoreHomeTeam()) &&
                (match.getScoreAwayTeam() == match.getScoreHomeTeam());
    }


    public double getExtraPointsForWinningTeam() {
        if (winningTeamIdByUser != 0) {
            if (winningTeamIdByUser == winningTeamId) {
                extraPoints = 30.0;
            }
            if (winningTeamIdByUser == secondTeamId) {
                extraPoints = 10.0;
            }
        }
        return extraPoints;
    }


    public Bet getBet() {
        return bet;
    }
}
