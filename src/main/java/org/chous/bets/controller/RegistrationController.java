package org.chous.bets.controller;

import org.chous.bets.model.User;
import org.chous.bets.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.chous.bets.service.RegistrationService;

import javax.validation.Valid;


@Controller
public class RegistrationController {

    private final RegistrationService registrationService;
    private final UserValidator userValidator;

    @Autowired
    public RegistrationController(RegistrationService registrationService, UserValidator userValidator) {
        this.registrationService = registrationService;
        this.userValidator = userValidator;
    }


    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }


    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") User user) {
        return "auth/registration";
    }


    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                          String password, String passwordConfirm) {

        userValidator.validate(user, bindingResult);
        userValidator.checkEquality(password, passwordConfirm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/auth/registration";
        }

        registrationService.register(user);

        return "auth/activation-message";
    }


    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = registrationService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message", "Вы подтвердили email. Теперь входите :)");
        } else {
            model.addAttribute("message", "Активационный код не найден или просрочен. Попробуйте восстановить пароль ещё раз");
        }

        return "auth/login";
    }


    @GetMapping("/reset-password")
    public String showEmailFormForResetPassword() {
        return "auth/reset-password";
    }


    @PostMapping("/reset-password")
    public String processEmailFormForResetPassword(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/auth/reset-password";
        }

        String email = user.getEmail();

        registrationService.updateResetPasswordToken(email);

        return "auth/reset-message";
    }


    @GetMapping("/reset-form/{token}")
    public String showResetPasswordForm(Model model, @PathVariable String token) {
        model.addAttribute("token", token);
        return "auth/reset-form";
    }


    @PostMapping("/reset-form/{token}")
    public String processResetPasswordForm(Model model, @ModelAttribute("user") @Valid User user1,
                                           BindingResult bindingResult, @PathVariable String token,
                                           String password, String passwordConfirm) {

        User user = registrationService.getByResetPasswordToken(token);
        model.addAttribute("user", user);

        userValidator.checkEquality(password, passwordConfirm, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Пароли не совпадают");
            return "auth/reset-form" ;
        }

        try {
            registrationService.updatePassword(user, password);
            model.addAttribute("message", "Вы успешно сбросили пароль");
        }
        catch(UsernameNotFoundException ex) {
            model.addAttribute("message", "Время действия ссылки на восстановление пароля истекло. Попробуйте ещё раз.");
        }

        return "auth/login";
    }

    @GetMapping("/profile")
    public String profilePage(@ModelAttribute("user") User user){
        return "profile";
    }

}