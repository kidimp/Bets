package org.chous.bets.controllers;

import org.chous.bets.models.User;
import org.chous.bets.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.chous.bets.services.RegistrationService;

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
            model.addAttribute("message", "User successfully activated!");
            System.out.println("User is activated!");
        } else {
            model.addAttribute("message", "Activation code is not found!");
            System.out.println("Activation code is not found!");
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

        registrationService.updatePassword(user, password);

        model.addAttribute("message", "You have successfully changed your password.");

        return "auth/login";
    }

}