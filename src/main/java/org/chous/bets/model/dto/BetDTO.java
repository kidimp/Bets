package org.chous.bets.model.dto;

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
    private int scoreHomeTeam;
    private int scoreAwayTeam;
    private boolean extraTime;
    private boolean penalty;
    private double points;
}
