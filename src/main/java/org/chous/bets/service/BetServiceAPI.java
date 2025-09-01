package org.chous.bets.service;

import org.chous.bets.model.dto.BetDTO;
import org.chous.bets.model.entity.Bet;

import java.util.List;

public interface BetServiceAPI {

    Bet getOrCreateBetForUserAndMatch(int userId, int matchId);

    List<BetDTO> getBets();

    void saveOrUpdateBet(int userId, int matchId, BetDTO betDTO);
}
