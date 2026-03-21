package org.chous.bets.service.impl;

import org.chous.bets.model.entity.Bet;
import org.chous.bets.model.entity.Match;
import org.chous.bets.model.entity.Team;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PointsCalculator {

    private final Bet bet;
    private final Match match;
    private final Team homeTeam;
    private final Team awayTeam;

    private final int winningTeamId;
    private final int secondTeamId;
    private final int winningTeamIdByUser;

    // Конструктор для расчёта по ставке и матчу
    public PointsCalculator(Bet bet, Match match, Team homeTeam, Team awayTeam) {
        this.bet = bet;
        this.match = match;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.winningTeamId = 1;
        this.secondTeamId = 2;
        this.winningTeamIdByUser = 0;
    }

    // Конструктор для расчёта экстра-бонусов
    public PointsCalculator(int winningTeamId, int secondTeamId, int winningTeamIdByUser) {
        this.bet = null;
        this.match = null;
        this.homeTeam = null;
        this.awayTeam = null;
        this.winningTeamId = winningTeamId;
        this.secondTeamId = secondTeamId;
        this.winningTeamIdByUser = winningTeamIdByUser;
    }

    public double getPointsForMatch() {
        if (bet == null || match == null) return 0.0;

        double points = 0.0;

        if (!bet.getMatch().getId().equals(match.getId())) return 0.0;

        boolean isDraw = match.getScoreHomeTeam() == match.getScoreAwayTeam();

        // Полное совпадение счёта
        if (isHitOnTheCorrectScore()) {
            points = 5;
        } else if (isHitOnTheMatchResult()) {
            int totalDiff = Math.abs(bet.getScoreHomeTeam() - match.getScoreHomeTeam())
                    + Math.abs(bet.getScoreAwayTeam() - match.getScoreAwayTeam());

            if (!isDraw) {
                if (totalDiff == 1) points = 4;
                else if (totalDiff == 2) points = 3;
                else points = 2;
            } else {
                int goalDiff = Math.abs((bet.getScoreHomeTeam() + bet.getScoreAwayTeam()) -
                        (match.getScoreHomeTeam() + match.getScoreAwayTeam()));

                if (goalDiff == 2) points = 4;
                else if (goalDiff == 4) points = 3;
                else if (goalDiff >= 6) points = 2;
            }
        }

        // Частичное угадывание
        if (!isHitOnTheMatchResult()) {
            if (bet.getScoreHomeTeam() == match.getScoreHomeTeam() ||
                    bet.getScoreAwayTeam() == match.getScoreAwayTeam()) {
                points = Math.max(points, 1);
            }

            if (bet.getScoreHomeTeam() == match.getScoreAwayTeam() &&
                    bet.getScoreAwayTeam() == match.getScoreHomeTeam()) {
                points = Math.max(points, 1);
            }
        }

        // Дополнительное время / пенальти
        if (bet.isExtraTime()) {
            points += match.isExtraTime() ? 5 : -2;
        }

        if (bet.isPenalty()) {
            points += match.isPenalty() ? 5 : -3;
        }

        // Множитель по рейтингу УЕФА
        if (isHitOnTheMatchResult() && homeTeam != null && awayTeam != null) {
            int potDiff = Math.abs(homeTeam.getPot() - awayTeam.getPot());

            boolean isUnderdogWin =
                    (match.getScoreHomeTeam() > match.getScoreAwayTeam() && homeTeam.getPot() > awayTeam.getPot()) ||
                            (match.getScoreAwayTeam() > match.getScoreHomeTeam() && awayTeam.getPot() > homeTeam.getPot());

            if (isUnderdogWin) {
                switch (potDiff) {
                    case 1 -> points *= 1.3;
                    case 2 -> points *= 1.5;
                    case 3 -> points *= 2;
                }
            }

            if (isDraw) {
                points *= 1.3;
            }
        }

        return Math.max(0, points);
    }

    public boolean isHitOnTheCorrectScore() {
        return bet.getScoreHomeTeam() == match.getScoreHomeTeam() &&
                bet.getScoreAwayTeam() == match.getScoreAwayTeam();
    }

    public boolean isHitOnTheMatchResult() {
        int betDiff = Integer.compare(bet.getScoreHomeTeam(), bet.getScoreAwayTeam());
        int matchDiff = Integer.compare(match.getScoreHomeTeam(), match.getScoreAwayTeam());
        return betDiff == matchDiff;
    }

    public double getExtraPointsForWinningTeam() {
        if (winningTeamIdByUser == 0) return 0.0;
        if (winningTeamIdByUser == winningTeamId) return 30.0;
        if (winningTeamIdByUser == secondTeamId) return 10.0;
        return 0.0;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
