package org.chous.bets.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO, представляющий строку таблицы лидеров.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class LeaderboardTableRowDTO {

    private String username;
    private double position;
    private double totalPointsFirstRound;
    private double totalPointsSecondRound;
    private double totalPointsThirdRound;
    private double totalPointsKnockoutRound;
    private double totalPointsAllRounds;
    private int numberOfHitsOnCorrectScore;
    private int numberOfHitsOnMatchResult;
    private String winningTeamPrediction;
    private boolean predictAvailable;
    private double extraPoints;
}
