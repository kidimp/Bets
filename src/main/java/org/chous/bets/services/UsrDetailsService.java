package org.chous.bets.services;

import org.chous.bets.models.User;
import org.chous.bets.repositories.UsersRepository;
import org.chous.bets.security.UsrDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsrDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;

    @Autowired
    public UsrDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = usersRepository.findByUsername(username);

        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new UsrDetails(user.get());
    }
}