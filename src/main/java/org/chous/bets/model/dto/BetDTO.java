package org.chous.bets.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BetDTO {

    private Integer id;
    private Integer userId;
    private Integer matchId;

    @NotNull(message = "Введите счёт")
    @Min(value = 0, message = "Счёт не может быть отрицательным")
    private Integer scoreHomeTeam;

    @NotNull(message = "Введите счёт")
    @Min(value = 0, message = "Счёт не может быть отрицательным")
    private Integer scoreAwayTeam;

    private boolean extraTime;
    private boolean penalty;
    private double points;
}
