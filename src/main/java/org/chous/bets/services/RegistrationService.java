package org.chous.bets.services;

import org.chous.bets.models.User;
import org.chous.bets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegistrationService {

    private final UsersRepository usersRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UsersRepository usersRepository, MailService mailService, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public void register(User user) {
        user.setEmail(user.getEmail());
        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setUsername(user.getUsername());

        user.setActivationCode(UUID.randomUUID().toString());

        if (!StringUtils.isEmpty(user.getEmail())) {
            //Атрымаць фактычны url без залежнасці ад размяшчэння сайта.
            final String baseUrl = "https://bets.pras.by";//ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

            String subject = "Пожалуйста, активируйте ваш аккаунт";
            String text = String.format(
                    "Привет, %s! \n" +
                            "Добро пожаловать в Bets. Пожалуйста, перейдите по этой ссылке, " +
                            "чтобы активировать ваш аккаунт: " +baseUrl +"/activate/%s",
                    user.getUsername(), user.getActivationCode()
            );

            mailService.send(user.getEmail(), subject, text);
        }

        usersRepository.save(user);
    }


    public boolean activateUser(String code) {
        User user = usersRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setIsActive(true);
        user.setActivationCode(null);

        usersRepository.save(user);

        return true;
    }


    @Transactional
    public void updateResetPasswordToken(String email) {
        Optional<User> user = usersRepository.findByEmail(email);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        user.get().setResetPasswordToken(UUID.randomUUID().toString());

        if (!StringUtils.isEmpty(user.get().getEmail())) {
            //Атрымаць фактычны url без залежнасці ад размяшчэння сайта.
            final String baseUrl = "https://bets.pras.by";//ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

            String subject = "Пожалуйста, установите новый пароль для вашей учетной записи";
            String text = String.format(
                    "Привет, %s! \n" +
                            "Пожалуйста, перейдите по этой ссылке, чтобы установить новый пароль" +
                            " для вашей учётной записи: " +baseUrl +"/reset-form/%s",
                    user.get().getUsername(), user.get().getResetPasswordToken()
            );

            mailService.send(user.get().getEmail(), subject, text);
        }

        usersRepository.save(user.get());
    }


    public User getByResetPasswordToken(String token) {
        return usersRepository.findByResetPasswordToken(token);
    }


    public void updatePassword(User user, String newPassword) {
        if (user == null) {
            //TODO: дадаць апрацоўку exception у кантролере пры скідзе пароля
            throw new UsernameNotFoundException("User not found");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);

        usersRepository.save(user);
    }

}