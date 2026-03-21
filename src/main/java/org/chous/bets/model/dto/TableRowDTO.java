package org.chous.bets.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 *  DTO, представляющий строку для таблиц раундов группового этапа, нокаут-стадии и таблицы, в которой собраны вместе все матчи турнира.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class TableRowDTO {

    @NotBlank
    @Size(min = 2, max = 100)
    private String username;

    private double totalPointsForThisRound;

    private int numberOfHitsOnCorrectScore;

    private int numberOfHitsOnMatchResult;

    private String winningTeamPrediction;

    private boolean predictAvailable;

    private double extraPoints;

    private List<BetViewDTO> betViews;
}
