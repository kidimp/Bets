package org.chous.bets.controller.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.controller.RegistrationControllerAPI;
import org.chous.bets.model.dto.PasswordUpdateDTO;
import org.chous.bets.model.dto.RegistrationRequestDTO;
import org.chous.bets.model.dto.ResetPasswordRequestDTO;
import org.chous.bets.model.dto.UserDTO;
import org.chous.bets.service.RegistrationServiceAPI;
import org.chous.bets.validator.UserValidator;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class RegistrationControllerImpl implements RegistrationControllerAPI {

    private final RegistrationServiceAPI registrationService;
    private final UserValidator userValidator;

    @Override
    public String login() {
        return "auth/login";
    }

    @Override
    public String registrationForm(RegistrationRequestDTO registrationRequest) {
        return "auth/registration";
    }

    @Override
    public String register(RegistrationRequestDTO registrationRequest,
                           BindingResult bindingResult,
                           String password, String passwordConfirm) {

        userValidator.validate(registrationRequest, bindingResult);
        userValidator.checkEquality(password, passwordConfirm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }

        registrationService.register(registrationRequest, password);
        return "auth/activation-message";
    }

    @Override
    public String activate(Model model, String code) {
        boolean isActivated = registrationService.activateUser(code);
        model.addAttribute("message", isActivated ?
                "Вы подтвердили email. Теперь входите :)" :
                "Активационный код не найден или просрочен. Попробуйте восстановить пароль ещё раз");
        return "auth/login";
    }

    @Override
    public String showResetPasswordForm() {
        return "auth/reset-password";
    }

    @Override
    public String processResetPassword(ResetPasswordRequestDTO request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "auth/reset-password";
        }

        registrationService.updateResetPasswordToken(request.getEmail());
        return "auth/reset-message";
    }

    @Override
    public String showResetPasswordForm(String token, Model model) {
        model.addAttribute("token", token);
        return "auth/reset-form";
    }

    @Override
    public String processResetPasswordForm(PasswordUpdateDTO passwordUpdate,
                                           BindingResult bindingResult,
                                           String token, String password,
                                           String passwordConfirm, Model model) {
        UserDTO user = registrationService.getByResetPasswordToken(token);

        userValidator.checkEquality(password, passwordConfirm, bindingResult);

        if (bindingResult.hasErrors()) {
            String firstError = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse("Пароли не совпадают или введены некорректно");
            model.addAttribute("error", firstError);
            return "auth/reset-form";
        }

        try {
            registrationService.updatePassword(user, password);
            model.addAttribute("message", "Вы успешно сбросили пароль");
        } catch (UsernameNotFoundException e) {
            model.addAttribute("message", "Время действия ссылки на восстановление пароля истекло. Попробуйте ещё раз.");
        }

        return "auth/login";
    }

    @Override
    public String profilePage(UserDTO user) {
        return "profile";
    }
}
