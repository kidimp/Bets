package org.chous.bets.service;

import org.chous.bets.model.dto.MatchDTO;

import java.util.List;

public interface MatchServiceAPI {

    List<MatchDTO> findAll();

    List<MatchDTO> findAllSortedByDateDesc();

    MatchDTO findById(Integer id);

    void save(MatchDTO matchDTO);

    void update(Integer id, MatchDTO matchDTO);

    void delete(Integer id);

    List<MatchDTO> findAllUpcomingAndStartedMatches();

    List<MatchDTO> findAllFinishedMatches();

    MatchDTO getMatchDTO(int matchId);
}
