package org.chous.bets.controller.user.impl;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chous.bets.controller.user.RegistrationControllerAPI;
import org.chous.bets.exception.DataNotFoundException;
import org.chous.bets.model.dto.PasswordUpdateDTO;
import org.chous.bets.model.dto.RegistrationRequestDTO;
import org.chous.bets.model.dto.ResetPasswordRequestDTO;
import org.chous.bets.model.dto.UserDTO;
import org.chous.bets.model.dto.UserNameUpdateDTO;
import org.chous.bets.service.RegistrationService;
import org.chous.bets.service.SecurityService;
import org.chous.bets.util.SecurityContextUtil;
import org.chous.bets.validator.UserValidator;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RegistrationControllerImpl implements RegistrationControllerAPI {

    private static final String MESSAGE = "message";

    private final RegistrationService registrationService;
    private final UserValidator userValidator;
    private final SecurityService securityService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @Override
    public String login(HttpSession session, Model model) {
        Object email = session.getAttribute("LAST_EMAIL");

        if (email != null) {
            model.addAttribute("email", email);
            session.removeAttribute("LAST_EMAIL");
        }

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
        model.addAttribute(MESSAGE, isActivated ?
                "Вы подтвердили email. Теперь входите :)" :
                "Активационный код не найден или просрочен. Попробуйте восстановить пароль ещё раз");
        return "auth/login";
    }

    @Override
    public String showResetPasswordRequestForm() {
        return "auth/reset-password";
    }

    @Override
    public String processResetPassword(ResetPasswordRequestDTO request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "auth/reset-password";
        }

        try {
            registrationService.updateResetPasswordToken(request.getEmail());
        } catch (DataNotFoundException e) {
            log.warn(e.getMessage());
        }
        return "auth/reset-message";
    }

    @Override
    public String showResetPasswordUpdateForm(String token, Model model) {
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
            model.addAttribute(MESSAGE, "Вы успешно сбросили пароль");
        } catch (UsernameNotFoundException e) {
            model.addAttribute(MESSAGE, "Время действия ссылки на восстановление пароля истекло. Попробуйте ещё раз.");
        }

        return "auth/login";
    }

    @Override
    public String profilePage(Model model) {
        UserDTO currentUser = registrationService.getCurrentUser();

        model.addAttribute("user", currentUser);
        model.addAttribute("email", currentUser.getEmail());

        return "profile";
    }

    @Override
    public String updateProfile(UserNameUpdateDTO user, BindingResult bindingResult, Model model) {
        String email = SecurityContextUtil.getPrincipal();
        UserDTO currentUser = registrationService.getCurrentUser();

        String newUsername = user.getUsername();
        String currentUsername = currentUser.getUsername();

        if (Objects.equals(newUsername, currentUsername)) {
            return "redirect:/";
        }

        userValidator.validateUsernameUpdate(newUsername, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("email", currentUser.getEmail());
            return "profile";
        }

        registrationService.updateUsername(newUsername, email);

        securityService.refreshAuthentication(email);

        return "redirect:/";
    }
}
