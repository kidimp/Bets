package org.chous.bets.service.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.mapper.MatchMapper;
import org.chous.bets.model.dto.MatchDTO;
import org.chous.bets.model.entity.Match;
import org.chous.bets.repository.BetRepository;
import org.chous.bets.repository.MatchRepository;
import org.chous.bets.service.MatchServiceAPI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchServiceImpl implements MatchServiceAPI {

    private final MatchRepository matchRepository;
    private final BetRepository betRepository;

    private final MatchMapper matchMapper;

    @Override
    public List<MatchDTO> findAll() {
        return matchRepository.findAll().stream()
                .map(matchMapper::toDto)
                .toList();
    }

    @Override
    public List<MatchDTO> findAllSortedByDateDesc() {
        return matchRepository.findAllByOrderByDateAndTimeDesc().stream()
                .map(matchMapper::toDto)
                .toList();
    }

    @Override
    public MatchDTO findById(Integer id) {
        return matchRepository.findById(id)
                .map(matchMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Матч не найден, id = " + id));
    }

    @Override
    @Transactional
    public void save(MatchDTO matchDTO) {
        matchRepository.save(matchMapper.toEntity(matchDTO));
    }

    @Override
    @Transactional
    public void update(Integer id, MatchDTO matchDTO) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Матч не найден, id = " + id));

        match.setDateAndTime(matchDTO.getDateAndTime());
        match.setStageId(matchDTO.getStageId());
        match.setRound(matchDTO.getRound());
        match.setHomeTeamId(matchDTO.getHomeTeamId());
        match.setAwayTeamId(matchDTO.getAwayTeamId());
        match.setScoreHomeTeam(matchDTO.getScoreHomeTeam());
        match.setScoreAwayTeam(matchDTO.getScoreAwayTeam());
        match.setFinished(matchDTO.isFinished());
        match.setExtraTime(matchDTO.isExtraTime());
        match.setPenalty(matchDTO.isPenalty());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        betRepository.deleteAllByMatchId(id);
        matchRepository.deleteById(id);
    }

    @Override
    public List<MatchDTO> findAllUpcomingAndStartedMatches() {
        LocalDateTime now = LocalDateTime.now();

        return matchRepository.findAllByFinishedFalseOrderByDateAndTimeAsc()
                .stream()
                .map(match -> {
                    MatchDTO dto = matchMapper.toDto(match);
                    if (dto.getDateAndTime().isAfter(now)) {
                        dto.setStarted(false);
                    }
                    return dto;
                })
                .toList();
    }

    @Override
    public List<MatchDTO> findAllFinishedMatches() {
        return matchRepository.findAllByFinishedTrueOrderByDateAndTimeDesc().stream()
                .map(matchMapper::toDto)
                .toList();
    }

    @Override
    public MatchDTO getMatchDTO(int matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Матч не найден"));

        if (LocalDateTime.now().isAfter(match.getDateAndTime())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нельзя ставить на начавшийся матч");
        }

        return matchMapper.toDto(match);
    }
}
