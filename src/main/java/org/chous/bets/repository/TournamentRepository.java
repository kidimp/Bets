package org.chous.bets.repository;

import org.chous.bets.model.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TournamentRepository extends JpaRepository<Tournament, Integer> {

    List<Tournament> findAllByOrderByStartDesc();
}
