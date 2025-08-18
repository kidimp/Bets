package org.chous.bets.service;

import org.chous.bets.model.dto.WinningTeamPredictionDTO;
import org.chous.bets.model.dto.WinningTeamTournamentDTO;

public interface WinningTeamServiceAPI {

    Integer getUserWinningTeamIdPrediction(Integer userId);

    String getUserWinningTeamNamePrediction(Integer userId);

    String getWinningTeamPredictionDateAndTimeDeadline();

    boolean isWinningTeamPredictionAvailable();

    boolean isWinningTeamPredictionByUserSet(Integer userId);

    void submitWinningTeamPrediction(Integer userId, WinningTeamPredictionDTO dto);

    WinningTeamTournamentDTO getTournamentWinningTeam();

    void submitTournamentWinningTeam(WinningTeamTournamentDTO dto);
}
