package org.chous.bets.model.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *  DTO, представляющий ставку игрока в таблице.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BetViewDTO {

    private boolean matchStarted;

    private boolean matchFinished;

    @Min(value = 0, message = "Счёт не может быть отрицательным")
    private int scoreHomeTeam;

    @Min(value = 0, message = "Счёт не может быть отрицательным")
    private int scoreAwayTeam;

    private boolean extraTime;

    private boolean penalty;

    private double points;
}
