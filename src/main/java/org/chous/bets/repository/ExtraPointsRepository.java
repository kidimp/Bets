package org.chous.bets.repository;

import jakarta.transaction.Transactional;
import org.chous.bets.model.entity.ExtraPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExtraPointsRepository extends JpaRepository<ExtraPoints, Long> {

    /**
     * Возвращает ID команды, на которую пользователь поставил как на будущего победителя турнира.
     * @param userId идентификатор пользователя
     * @return идентификатор выбранной команды
     */
    @Query("SELECT e.winningTeamId FROM ExtraPoints e WHERE e.userId = :userId")
    Optional<Integer> findWinningTeamIdByUserId(Integer userId);

    Optional<ExtraPoints> findByUserId(Integer userId);

    List<ExtraPoints> findByUserIdIn(Collection<Integer> userIds);

    @Modifying
    @Transactional
    @Query("UPDATE ExtraPoints e SET e.extraPoints = :points WHERE e.userId = :userId")
    void updateExtraPointsByUserId(@Param("userId") int userId, @Param("points") double points);

    @Transactional
    @Modifying
    @Query("DELETE FROM ExtraPoints ep WHERE ep.winningTeamId = :teamId")
    void deleteAllByTeamId(@Param("teamId") Integer teamId);
}
