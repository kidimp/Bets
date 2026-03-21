package org.chous.bets.service;

import org.chous.bets.model.dto.TournamentDTO;

import java.util.List;

public interface TournamentService {

    List<TournamentDTO> findAllSortedByDateDesc();
}
