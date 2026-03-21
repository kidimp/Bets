package org.chous.bets.repository;

import org.chous.bets.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Integer id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByActivationCode(String code);

    Optional<User> findByResetPasswordToken(String token);

    /**
     * Юзер становится активным, как только он сделал свою превую ставку в текущем турнире
     * @return список активных юзеров
     */
    @Query(value = "SELECT * FROM users WHERE id IN (SELECT DISTINCT user_id FROM bet)", nativeQuery = true)
    List<User> findAllActiveUsers();
}