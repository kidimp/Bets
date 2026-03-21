package org.chous.bets.repository;

import jakarta.transaction.Transactional;
import org.chous.bets.model.entity.WinningTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WinningTeamRepository extends JpaRepository<WinningTeam, Integer> {

    @Query("SELECT w FROM WinningTeam w WHERE w.id = :id")
    Optional<WinningTeam> getTournamentWinningTeam(@Param("id") Integer id);

    @Query("SELECT w.dateAndTime FROM WinningTeam w WHERE w.id = :id")
    Optional<LocalDateTime> getWinningTeamPredictionDateAndTimeDeadline(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("DELETE FROM WinningTeam wt WHERE wt.winningTeamId = :id OR wt.secondPlaceTeamId = :id")
    void deleteAllByTeamId(@Param("id") int id);
}
