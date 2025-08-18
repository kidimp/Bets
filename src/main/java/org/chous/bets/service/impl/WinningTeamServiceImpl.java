package org.chous.bets.service.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.exception.DataNotFoundException;
import org.chous.bets.mapper.WinningTeamMapper;
import org.chous.bets.model.dto.WinningTeamPredictionDTO;
import org.chous.bets.model.dto.WinningTeamTournamentDTO;
import org.chous.bets.model.entity.ExtraPoints;
import org.chous.bets.model.entity.Team;
import org.chous.bets.model.entity.WinningTeam;
import org.chous.bets.repository.ExtraPointsRepository;
import org.chous.bets.repository.TeamRepository;
import org.chous.bets.repository.WinningTeamRepository;
import org.chous.bets.service.WinningTeamServiceAPI;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.chous.bets.util.Constants.TOURNAMENT_WINNING_TEAM_ID;

@Service
@RequiredArgsConstructor
public class WinningTeamServiceImpl implements WinningTeamServiceAPI {

    private final ExtraPointsRepository extraPointsRepository;
    private final WinningTeamRepository winningTeamRepository;
    private final TeamRepository teamRepository;
    private final WinningTeamMapper winningTeamMapper;

    private Team getUserWinningTeamPrediction(Integer userId) {
        Integer winningTeamIdByUser = extraPointsRepository.findWinningTeamIdByUserId(userId)
                .orElse(0);

        return teamRepository.findById(winningTeamIdByUser)
                .orElse(new Team(0, "", "", 0));
    }

    @Override
    public Integer getUserWinningTeamIdPrediction(Integer userId) {
        return getUserWinningTeamPrediction(userId).getId();
    }

    @Override
    public String getUserWinningTeamNamePrediction(Integer userId) {
        return getUserWinningTeamPrediction(userId).getName();
    }

    private LocalDateTime getWinningTeamPredictionDeadline() {
        return winningTeamRepository.getWinningTeamPredictionDateAndTimeDeadline(TOURNAMENT_WINNING_TEAM_ID)
                .orElseThrow(() -> new DataNotFoundException("Winning team prediction deadline not found"));
    }

    @Override
    public String getWinningTeamPredictionDateAndTimeDeadline() {
        return getWinningTeamPredictionDeadline()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    @Override
    public boolean isWinningTeamPredictionAvailable() {
        return LocalDateTime.now().isBefore(getWinningTeamPredictionDeadline());
    }

    @Override
    public boolean isWinningTeamPredictionByUserSet(Integer userId) {
        return getUserWinningTeamPrediction(userId).getId() > 0;
    }

    @Override
    public void submitWinningTeamPrediction(Integer userId, WinningTeamPredictionDTO dto) {
        Team team = teamRepository.findById(dto.getWinningTeamId())
                .orElseThrow(() -> new DataNotFoundException("Team not found"));

        ExtraPoints extraPoints = extraPointsRepository.findByUserId(userId)
                .orElseGet(() -> {
                    ExtraPoints newEntry = new ExtraPoints();
                    newEntry.setUserId(userId);
                    return newEntry;
                });

        extraPoints.setWinningTeamId(team.getId());
        extraPointsRepository.save(extraPoints);
    }

    @Override
    public WinningTeamTournamentDTO getTournamentWinningTeam() {
        return winningTeamRepository
                .getTournamentWinningTeam(TOURNAMENT_WINNING_TEAM_ID)
                .map(winningTeamMapper::toDto)
                .orElseGet(() -> {
                    WinningTeamTournamentDTO wt = new WinningTeamTournamentDTO();
                    wt.setDateAndTime(LocalDateTime.MAX);
                    wt.setWinningTeamId(null);  // todo проверить этот случай
                    wt.setSecondPlaceTeamId(null);
                    return wt;
                });
    }

    @Override
    public void submitTournamentWinningTeam(WinningTeamTournamentDTO winningTeamTournamentDTO) {
        WinningTeam entity = winningTeamMapper.toEntity(winningTeamTournamentDTO);
        entity.setId(TOURNAMENT_WINNING_TEAM_ID);
        winningTeamRepository.save(entity);
    }
}
