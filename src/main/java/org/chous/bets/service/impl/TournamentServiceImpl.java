package org.chous.bets.service.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.mapper.TournamentMapper;
import org.chous.bets.model.dto.TournamentDTO;
import org.chous.bets.repository.TournamentRepository;
import org.chous.bets.service.TournamentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;
    private final TournamentMapper tournamentMapper;

    @Override
    public List<TournamentDTO> findAllSortedByDateDesc() {
        return tournamentRepository.findAllByOrderByStartDesc().stream()
                .map(tournamentMapper::toDto)
                .toList();
    }
}
