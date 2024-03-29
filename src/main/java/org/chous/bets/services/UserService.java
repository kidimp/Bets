package org.chous.bets.services;

import org.chous.bets.models.*;
import org.chous.bets.repositories.UsersRepository;
import org.chous.bets.security.UsrDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private static UsersRepository usersRepository;

    @Autowired
    public UserService(UsersRepository usersRepository) {
        UserService.usersRepository = usersRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = usersRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return new UsrDetails(user.get());
    }


    public static void getCurrentPrincipalUserRole(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        if (usersRepository.findByUsername(username).isPresent()) {
            model.addAttribute("role", usersRepository.findByUsername(username).get().getRole());
        } else {
            model.addAttribute("role", "ROLE_USER");
        }
    }


    public static int getCurrentPrincipalUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Objects.requireNonNull(usersRepository.findByUsername(authentication.getName())
                .orElse(null)).getId();
    }

}