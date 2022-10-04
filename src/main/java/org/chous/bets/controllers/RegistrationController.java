package org.chous.bets.controllers;


import org.chous.bets.models.User;
import org.chous.bets.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/auth/registration";
        }

        registrationService.register(user);

        return "auth/login";
    }
}