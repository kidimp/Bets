package org.chous.bets.PointsCalculator;

import org.chous.bets.model.entity.Bet;
import org.chous.bets.model.entity.Match;
import org.chous.bets.model.entity.Team;
import org.chous.bets.service.impl.PointsCalculator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PointsCalculatorTest {

    static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of("Угадан точный счёт, без множителя",
                        createBet(2, 1, false, false),
                        createMatch(2, 1, false, false),
                        createTeam(1, 1), createTeam(2, 1),
                        5.0),

                Arguments.of("Угадан точный счёт, множитель 1.3",
                        createBet(2, 1, false, false),
                        createMatch(2, 1, false, false),
                        createTeam(1, 2), createTeam(2, 1),
                        5.0 * 1.3),

                Arguments.of("Угадан точный счёт, множитель 1.5",
                        createBet(2, 1, false, false),
                        createMatch(2, 1, false, false),
                        createTeam(1, 3), createTeam(2, 1),
                        5.0 * 1.5),

                Arguments.of("Угадан точный счёт, множитель 2.0",
                        createBet(2, 1, false, false),
                        createMatch(2, 1, false, false),
                        createTeam(1, 4), createTeam(2, 1),
                        5.0 * 2.0),
                //----------------------------------------------------------------------------------------------------
                Arguments.of("Угадан результат матча, погрешность 1 гол, без множителя",
                        createBet(2, 0, false, false),
                        createMatch(2, 1, false, false),
                        createTeam(1, 1), createTeam(2, 1),
                        4.0),

                Arguments.of("Угадан результат матча, погрешность 2 гола, без множителя",
                        createBet(2, 0, false, false),
                        createMatch(3, 1, false, false),
                        createTeam(1, 1), createTeam(2, 1),
                        3.0),

                Arguments.of("Угадан результат матча, погрешность > 2 голов, без множителя",
                        createBet(2, 0, false, false),
                        createMatch(5, 0, false, false),
                        createTeam(1, 1), createTeam(2, 1),
                        2.0),
                //----------------------------------------------------------------------------------------------------
                Arguments.of("Угадан результат матча, погрешность 1 гол, множитель 1.3",
                        createBet(2, 0, false, false),
                        createMatch(2, 1, false, false),
                        createTeam(1, 2), createTeam(2, 1),
                        4.0 * 1.3),

                Arguments.of("Угадан результат матча, погрешность 2 гола, множитель 1.5",
                        createBet(2, 0, false, false),
                        createMatch(3, 1, false, false),
                        createTeam(1, 3), createTeam(2, 1),
                        3.0 * 1.5),

                Arguments.of("Угадан результат матча, погрешность > 2 голов, множитель 2.0",
                        createBet(2, 0, false, false),
                        createMatch(5, 0, false, false),
                        createTeam(1, 4), createTeam(2, 1),
                        2.0 * 2.0),
                //----------------------------------------------------------------------------------------------------
                Arguments.of("Неверный результат, один гол угадан",
                        createBet(2, 3, false, false),
                        createMatch(2, 1, false, false),
                        createTeam(1, 2), createTeam(2, 2),
                        1.0),
                //----------------------------------------------------------------------------------------------------
                Arguments.of("Ничья, угадан точный счёт, множитель 1.3",
                        createBet(1, 1, false, false),
                        createMatch(1, 1, false, false),
                        createTeam(1, 1), createTeam(2, 2),
                        5.0 * 1.3),

                Arguments.of("Ничья, угадан результат матча, ошибка на 1 гол (2:2 вместо 1:1), множитель 1.3",
                        createBet(2, 2, false, false),
                        createMatch(1, 1, false, false),
                        createTeam(1, 1), createTeam(2, 2),
                        4.0 * 1.3),
                //----------------------------------------------------------------------------------------------------
                Arguments.of("Ничья, один гол угадан",
                        createBet(1, 1, false, false),
                        createMatch(2, 1, false, false),
                        createTeam(1, 1), createTeam(2, 1),
                        1.0),

                Arguments.of("Ничья, один гол угадан, есть андердог",
                        createBet(1, 1, false, false),
                        createMatch(2, 1, false, false),
                        createTeam(1, 1), createTeam(2, 4),
                        1.0),
                //----------------------------------------------------------------------------------------------------
                Arguments.of("Обратный счёт",
                        createBet(1, 2, false, false),
                        createMatch(2, 1, false, false),
                        createTeam(1, 2), createTeam(2, 2),
                        1.0),

                Arguments.of("Обратный счёт, победил андердог",
                        createBet(1, 2, false, false),
                        createMatch(2, 1, false, false),
                        createTeam(1, 4), createTeam(2, 1),
                        1.0),
                //----------------------------------------------------------------------------------------------------
                Arguments.of("Точный счёт, были Extra Time и Penalty, угадано Extra Time и Penalty, без множителя",
                        createBet(2, 1, true, true),
                        createMatch(2, 1, true, true),
                        createTeam(1, 1), createTeam(2, 1),
                        5 + 5 + 5),

                Arguments.of("Точный счёт, были Extra Time и Penalty, угадано Extra Time и Penalty, множитель 1.3",
                        createBet(2, 1, true, true),
                        createMatch(2, 1, true, true),
                        createTeam(1, 2), createTeam(2, 1),
                        (5 + 5 + 5) * 1.3),

                Arguments.of("Точный счёт, были Extra Time и Penalty, угадано Extra Time и Penalty, множитель 1.5",
                        createBet(2, 1, true, true),
                        createMatch(2, 1, true, true),
                        createTeam(1, 3), createTeam(2, 1),
                        (5 + 5 + 5) * 1.5),

                Arguments.of("Точный счёт, были Extra Time и Penalty, угадано Extra Time и Penalty, множитель 2.0",
                        createBet(2, 1, true, true),
                        createMatch(2, 1, true, true),
                        createTeam(1, 4), createTeam(2, 1),
                        (5 + 5 + 5) * 2.0),
                //----------------------------------------------------------------------------------------------------
                Arguments.of("Точный счёт, был Extra Time, угадано Extra Time, без множителя",
                        createBet(2, 1, true, false),
                        createMatch(2, 1, true, false),
                        createTeam(1, 1), createTeam(2, 1),
                        5 + 5),

                Arguments.of("Точный счёт, был Extra Time, угадано Extra Time, угадано Extra Time, множитель 1.3",
                        createBet(2, 1, true, false),
                        createMatch(2, 1, true, false),
                        createTeam(1, 2), createTeam(2, 1),
                        (5 + 5) * 1.3),

                Arguments.of("Точный счёт, был Extra Time, угадано Extra Time, угадано Extra Time, множитель 1.5",
                        createBet(2, 1, true, false),
                        createMatch(2, 1, true, false),
                        createTeam(1, 3), createTeam(2, 1),
                        (5 + 5) * 1.5),

                Arguments.of("Точный счёт, был Extra Time, угадано Extra Time, угадано Extra Time, множитель 2.0",
                        createBet(2, 1, true, false),
                        createMatch(2, 1, true, false),
                        createTeam(1, 4), createTeam(2, 1),
                        (5 + 5) * 2.0),
                //----------------------------------------------------------------------------------------------------
                Arguments.of("Точный счёт, не было Extra Time и Penalty, в ставке Extra Time и Penalty, без множителя",
                        createBet(2, 1, true, true),
                        createMatch(2, 1, false, false),
                        createTeam(1, 1), createTeam(2, 1),
                        5 - 2 - 3),

                Arguments.of("Точный счёт, не было Extra Time и Penalty, в ставке Extra Time и Penalty, множитель 1.3",
                        createBet(2, 1, true, true),
                        createMatch(2, 1, false, false),
                        createTeam(1, 2), createTeam(2, 1),
                        (5 - 2 - 3) * 1.3),

                Arguments.of("Точный счёт, не было Extra Time и Penalty, в ставке Extra Time и Penalty, множитель 1.5",
                        createBet(2, 1, true, true),
                        createMatch(2, 1, false, false),
                        createTeam(1, 3), createTeam(2, 1),
                        (5 - 2 - 3) * 1.5),

                Arguments.of("Точный счёт, не было Extra Time и Penalty, в ставке Extra Time и Penalty, множитель 2.0",
                        createBet(2, 1, true, true),
                        createMatch(2, 1, false, false),
                        createTeam(1, 4), createTeam(2, 1),
                        (5 - 2 - 3) * 2.0),
                //----------------------------------------------------------------------------------------------------
                Arguments.of("Точный счёт, было Extra Time, но не было Penalty, в ставке Extra Time и Penalty, без множителя",
                        createBet(2, 1, true, true),
                        createMatch(2, 1, true, false),
                        createTeam(1, 1), createTeam(2, 1),
                        5 + 5 - 3),

                Arguments.of("Точный счёт, было Extra Time, но не было Penalty, в ставке Extra Time и Penalty, множитель 1.3",
                        createBet(2, 1, true, true),
                        createMatch(2, 1, true, false),
                        createTeam(1, 2), createTeam(2, 1),
                        (5 + 5 - 3) * 1.3),

                Arguments.of("Точный счёт, было Extra Time, но не было Penalty, в ставке Extra Time и Penalty, множитель 1.5",
                        createBet(2, 1, true, true),
                        createMatch(2, 1, true, false),
                        createTeam(1, 3), createTeam(2, 1),
                        (5 + 5 - 3) * 1.5),

                Arguments.of("Точный счёт, было Extra Time, но не было Penalty, в ставке Extra Time и Penalty, множитель 2.0",
                        createBet(2, 1, true, true),
                        createMatch(2, 1, true, false),
                        createTeam(1, 4), createTeam(2, 1),
                        (5 + 5 - 3) * 2.0)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideTestCases")
    void testPointsCalculation(String name, Bet bet, Match match, Team home, Team away, double expected) {
        bet.setMatch(match);
        PointsCalculator calc = new PointsCalculator(bet, match, home, away);
        double actual = PointsCalculator.round(calc.getPointsForMatch(), 2);

        assertEquals(expected, actual, 0.01, "Ошибка в кейсе: " + name);
    }

    private static Bet createBet(int homeGoals, int awayGoals, boolean et, boolean pen) {
        Bet bet = new Bet();
        bet.setId(1);
        bet.setUser(null);
        bet.setMatch(null);
        bet.setScoreHomeTeam(homeGoals);
        bet.setScoreAwayTeam(awayGoals);
        bet.setExtraTime(et);
        bet.setPenalty(pen);
        return bet;
    }

    private static Match createMatch(int homeGoals, int awayGoals, boolean et, boolean pen) {
        Match match = new Match();
        match.setId(1);
        match.setDateAndTime(LocalDateTime.now());
        match.setStageId(1);
        match.setRound(1);
        match.setHomeTeamId(1);
        match.setAwayTeamId(2);
        match.setFinished(true);
        match.setScoreHomeTeam(homeGoals);
        match.setScoreAwayTeam(awayGoals);
        match.setExtraTime(et);
        match.setPenalty(pen);
        return match;
    }

    private static Team createTeam(int id, int pot) {
        Team team = new Team();
        team.setId(id);
        team.setName("Team" + id);
        team.setIsoName("T" + id);
        team.setPot(pot);
        return team;
    }
}