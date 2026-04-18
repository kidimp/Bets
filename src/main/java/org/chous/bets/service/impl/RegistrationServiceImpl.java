package org.chous.bets.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.chous.bets.exception.DataNotFoundException;
import org.chous.bets.mapper.UserMapper;
import org.chous.bets.model.dto.RegistrationRequestDTO;
import org.chous.bets.model.dto.UserDTO;
import org.chous.bets.model.entity.User;
import org.chous.bets.model.enums.RoleEnum;
import org.chous.bets.repository.UserRepository;
import org.chous.bets.service.MailService;
import org.chous.bets.service.RegistrationService;
import org.chous.bets.util.SecurityContextUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    @Value("${base-url}")
    private String baseUrl;

    private final MailService mailService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(RegistrationRequestDTO registrationRequest, String rawPassword) {
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(RoleEnum.USER);
        user.setActivationCode(UUID.randomUUID().toString());
        user.setActive(false);
        userRepository.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String activationLink = baseUrl + "/activate/" + user.getActivationCode();
            String subject = "Пожалуйста, активируйте ваш аккаунт";
            String text = String.format("""
                    Привет, %s!
                    
                    Добро пожаловать в Bets.
                    
                    Пожалуйста, перейдите по этой ссылке, чтобы активировать ваш аккаунт:
                    %s
                    """, user.getUsername(), activationLink);

            mailService.send(user.getEmail(), subject, text);
        }
    }

    @Override
    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code)
                .orElseThrow(() -> new DataNotFoundException("Пользователь по переданному коду активации не найден"));
        user.setActive(true);
        user.setActivationCode(null);
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public void updateResetPasswordToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("Пользователь по переданному email={} не найден", email));
        user.setResetPasswordToken(UUID.randomUUID().toString());
        userRepository.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String resetLink = baseUrl + "/reset-form/" + user.getResetPasswordToken();
            String subject = "Сброс пароля";
            String text = String.format("""
                    Привет, %s!
                    
                    Чтобы сбросить пароль, перейдите по ссылке:
                    %s
                    """, user.getUsername(), resetLink);

            mailService.send(user.getEmail(), subject, text);
        }
    }

    @Override
    public UserDTO getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token)
                .map(userMapper::toDto)
                .orElseThrow(() -> new DataNotFoundException("Пользователь по переданному токену не найден"));
    }

    @Override
    public void updatePassword(UserDTO userDTO, String newPassword) {
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new DataNotFoundException("Пользователь с id={} не найден", String.valueOf(userDTO.getId())));
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    @Override
    public UserDTO getCurrentUser() {
        String email = SecurityContextUtil.getPrincipal();
        return userRepository.findByEmail(email)
                .map(userMapper::toDto)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));
    }

    @Override
    public void updateUsername(String username, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        user.setUsername(username);

        userRepository.save(user);
    }
}
