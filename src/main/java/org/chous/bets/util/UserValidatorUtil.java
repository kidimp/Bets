package org.chous.bets.util;

import lombok.RequiredArgsConstructor;
import org.chous.bets.model.dto.RegistrationRequestDTO;
import org.chous.bets.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserValidatorUtil {

    private final UserRepository userRepository;

    public void validate(RegistrationRequestDTO registrationRequest, BindingResult bindingResult) {
        userRepository.findByEmail(registrationRequest.getEmail())
                .ifPresent(existingUser ->
                        bindingResult.rejectValue("email", "error.user", "Этот еmail уже занят")
                );

        userRepository.findByUsername(registrationRequest.getUsername())
                .ifPresent(existingUser ->
                        bindingResult.rejectValue("username", "error.user", "Этот никнейм уже занят")
                );
    }

    public void checkEquality(String password, String confirm, Errors errors) {
        if (!Objects.equals(password, confirm)) {
            errors.rejectValue("password", "error.password", "Пароли не совпадают!");
        }
    }
}
