package org.chous.bets.repositories;

import org.chous.bets.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    //Optional<User> findById(Integer id);
}