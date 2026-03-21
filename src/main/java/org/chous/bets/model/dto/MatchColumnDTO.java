package org.chous.bets.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *  DTO, представляющий информацию об отдельном футбольном матче в виде шапки колонки таблицы.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class MatchColumnDTO {

    @NotNull(message = "Домашняя команда обязательна")
    private String homeTeamName;

    @NotNull(message = "Гостевая команда обязательна")
    private String awayTeamName;

    @NotEmpty(message = "ISO name must not be empty")
    @Size(min = 2, max = 2, message = "ISO name must be exactly 2 characters")
    private String homeTeamIsoName;

    @NotEmpty(message = "ISO name must not be empty")
    @Size(min = 2, max = 2, message = "ISO name must be exactly 2 characters")
    private String awayTeamIsoName;

    private boolean finished;

    @Min(value = 0, message = "Счёт не может быть отрицательным")
    private int scoreHomeTeam;

    @Min(value = 0, message = "Счёт не может быть отрицательным")
    private int scoreAwayTeam;

    private boolean extraTime;

    private boolean penalty;
}
