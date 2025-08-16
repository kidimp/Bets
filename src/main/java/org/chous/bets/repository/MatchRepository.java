package org.chous.bets.repository;

import jakarta.transaction.Transactional;
import org.chous.bets.model.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {

    List<Match> findAllByOrderByDateAndTimeDesc();

    List<Match> findAllByFinishedFalseOrderByDateAndTimeAsc();

    List<Match> findAllByFinishedTrueOrderByDateAndTimeDesc();

    List<Match> findAllByOrderByDateAndTimeAsc();

    List<Match> findByRoundOrderByDateAndTimeAsc(int roundId);

    List<Match> findAllByRound(Integer id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Match m WHERE m.homeTeamId = :teamId OR m.awayTeamId = :teamId")
    void deleteAllByTeamId(@Param("teamId") Integer teamId);
}
