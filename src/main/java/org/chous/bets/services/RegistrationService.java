package org.chous.bets.services;

import org.chous.bets.models.User;
import org.chous.bets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final UsersRepository usersRepository;

    @Autowired
    public RegistrationService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Transactional
    public void register(User user) {
        user.setPassword(user.getPassword());
        user.setRole("ROLE_USER");
        usersRepository.save(user);
    }
}
