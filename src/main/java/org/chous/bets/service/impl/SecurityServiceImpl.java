package org.chous.bets.service.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.security.CustomUserDetailsService;
import org.chous.bets.service.SecurityService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final CustomUserDetailsService userDetailsService;

    @Override
    public void refreshAuthentication(String email) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        UserDetails updatedUser = userDetailsService.loadUserByUsername(email);

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                updatedUser,
                currentAuth.getCredentials(),
                updatedUser.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
