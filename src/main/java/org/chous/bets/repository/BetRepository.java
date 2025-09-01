package org.chous.bets.repository;

import jakarta.transaction.Transactional;
import org.chous.bets.model.entity.Bet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BetRepository extends JpaRepository<Bet, Integer>, BetRepositoryCustom {

    /**
     * Spring Data JPA подгружает user, потому что запрос по userId (Hibernate автоматически инициализирует user).
     * А match в запросе не участвует — поэтому остаётся LAZY и не загружается.
     * @param userId идентификатор пользователя
     * @return список ставок
     */
    @EntityGraph(attributePaths = {"match"})
    List<Bet> findByUserId(Integer userId);

    Optional<Bet> findByUserIdAndMatchId(Integer userId, Integer matchId);

    List<Bet> findByMatchIdIn(List<Integer> matchIds);

    @Transactional
    @Modifying
    @Query("DELETE FROM Bet b WHERE b.match.id = :matchId")
    void deleteAllByMatchId(@Param("matchId") Integer matchId);
}
