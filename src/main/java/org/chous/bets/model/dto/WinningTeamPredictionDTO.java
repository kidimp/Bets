package org.chous.bets.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * ДТО для сохранения и обновления ставки юзера на команду-победителя турнира
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class WinningTeamPredictionDTO {

    private Integer winningTeamId;
}
