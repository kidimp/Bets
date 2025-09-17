package org.chous.bets.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class BetRepositoryImpl implements BetRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void updatePoints(Integer betId, double points) {
        entityManager.createQuery("""
            UPDATE Bet b 
            SET b.points = :points 
            WHERE b.id = :betId
        """)
                .setParameter("points", points)
                .setParameter("betId", betId)
                .executeUpdate();
    }
}
