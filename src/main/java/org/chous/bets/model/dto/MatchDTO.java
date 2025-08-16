package org.chous.bets.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class MatchDTO {

    private Integer id;

    @NotNull(message = "Дата и время обязательны")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateAndTime;

    @NotNull(message = "ID стадии обязателен")
    private Integer stageId;

    @NotNull(message = "Раунд обязателен")
    private Integer round;

    @NotNull(message = "Домашняя команда обязательна")
    private Integer homeTeamId;

    @NotNull(message = "Гостевая команда обязательна")
    private Integer awayTeamId;

    private boolean started = true;

    private boolean finished;

    @Min(value = 0, message = "Счёт не может быть отрицательным")
    private int scoreHomeTeam;

    @Min(value = 0, message = "Счёт не может быть отрицательным")
    private int scoreAwayTeam;

    private boolean extraTime;

    private boolean penalty;
}
