package org.chous.bets.controller;

import jakarta.validation.Valid;
import org.chous.bets.model.dto.PasswordUpdateDTO;
import org.chous.bets.model.dto.RegistrationRequestDTO;
import org.chous.bets.model.dto.ResetPasswordRequestDTO;
import org.chous.bets.model.dto.UserDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface RegistrationControllerAPI {

    @GetMapping("/login")
    String login();

    @GetMapping("/registration")
    String registrationForm(@ModelAttribute("registrationRequest") RegistrationRequestDTO registrationRequest);

    @PostMapping("/registration")
    String register(@ModelAttribute("registrationRequest") @Valid RegistrationRequestDTO registrationRequest,
                    BindingResult bindingResult,
                    @RequestParam String password,
                    @RequestParam String passwordConfirm);

    @GetMapping("/activate/{code}")
    String activate(Model model, @PathVariable String code);

    @GetMapping("/reset-password")
    String showResetPasswordForm();

    @PostMapping("/reset-password")
    String processResetPassword(@Valid @ModelAttribute("request") ResetPasswordRequestDTO request,
                                BindingResult bindingResult);

    @GetMapping("/reset-form/{token}")
    String showResetPasswordForm(@PathVariable String token, Model model);

    @PostMapping("/reset-form/{token}")
    String processResetPasswordForm(@ModelAttribute("passwordUpdate") @Valid PasswordUpdateDTO passwordUpdate,
                                    BindingResult bindingResult,
                                    @PathVariable String token,
                                    @RequestParam String password,
                                    @RequestParam String passwordConfirm,
                                    Model model);

    @GetMapping("/profile")
    String profilePage(@ModelAttribute("user") UserDTO user);
}
