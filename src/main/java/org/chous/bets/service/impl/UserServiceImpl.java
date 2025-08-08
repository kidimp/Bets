package org.chous.bets.service.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.exception.DataNotFoundException;
import org.chous.bets.mapper.UserMapper;
import org.chous.bets.model.dto.UserDTO;
import org.chous.bets.model.dto.UserUpdateDTO;
import org.chous.bets.model.entity.User;
import org.chous.bets.repository.UserRepository;
import org.chous.bets.service.UserServiceAPI;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserServiceAPI, UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public Optional<UserDTO> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDto);
    }

    @Override
    public void update(String username, UserUpdateDTO updatedUser) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("Пользователь по переданному username=" + username + " не найден"));

        user.setUsername(updatedUser.getUsername());
        user.setRole(updatedUser.getRole());

        userRepository.save(user);
    }

    @Override
    public int getPrincipalUserId(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("Пользователь по переданному email=" + email + " не найден"));
        return user.getId();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
