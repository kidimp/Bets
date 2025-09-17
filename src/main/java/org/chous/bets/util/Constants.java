package org.chous.bets.util;

public class Constants {

    private Constants() {}

    /**
     * Особенность в том, что id всегда будет 1. Потому что мы в первый раз создаём запись в бд
     * о победителе турнира, а потом просто обновляем для каждого последующего турнира.
     */
    public static final int TOURNAMENT_WINNING_TEAM_ID = 1;

    public static final double AMOUNT_OF_ROUNDS = 5.0;
    public static final int FIRST_ROUND = 1;
    public static final int SECOND_ROUND = 2;
    public static final int THIRD_ROUND = 3;
    public static final int KNOCKOUT_STAGE = 4;
    public static final int ALL_STAGES_COMBINED = 0;
    public static final int LEADERSHIP_TABLE = 0;

    public static final String TEAMS_HAVE_DUPLICATES = "В одном турнире должны быть только уникальные команды";
}
