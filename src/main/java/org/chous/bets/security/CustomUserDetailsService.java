package org.chous.bets.security;

import org.chous.bets.model.entity.User;
import org.chous.bets.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        return new CustomUserDetails(user);
    }
}
// todo Found 2 UserDetailsService beans, with names [customUserDetailsService, userServiceImpl].
//  Global Authentication Manager will not use a UserDetailsService for username/password login.
//  Consider publishing a single UserDetailsService bean.