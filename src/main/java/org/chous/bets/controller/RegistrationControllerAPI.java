package org.chous.bets.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Registration", description = "Операции по регистрации, аутентификации, активации и восстановлению доступа")
public interface RegistrationControllerAPI {

    @Operation(
            summary = "Страница входа",
            description = "Возвращает страницу формы логина"
    )
    @GetMapping("/login")
    String login();

    @Operation(
            summary = "Страница регистрации",
            description = "Возвращает форму регистрации нового пользователя"
    )
    @GetMapping("/registration")
    String registrationForm(
            @Parameter(description = "DTO регистрации")
            @ModelAttribute("registrationRequest") RegistrationRequestDTO registrationRequest
    );

    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Обрабатывает форму регистрации. В случае ошибок возвращает форму обратно."
    )
    @PostMapping("/registration")
    String register(
            @Parameter(description = "Данные формы регистрации")
            @ModelAttribute("registrationRequest") @Valid RegistrationRequestDTO registrationRequest,
            BindingResult bindingResult,
            @Parameter(description = "Пароль") @RequestParam String password,
            @Parameter(description = "Подтверждение пароля") @RequestParam String passwordConfirm
    );

    @Operation(
            summary = "Активация пользователя",
            description = "Активирует аккаунт по коду из email"
    )
    @GetMapping("/activate/{code}")
    String activate(
            Model model,
            @Parameter(description = "Код активации") @PathVariable String code
    );

    @Operation(
            summary = "Страница восстановления пароля",
            description = "Отображает форму ввода email для отправки письма с токеном"
    )
    @GetMapping("/reset-password")
    String showResetPasswordRequestForm();

    @Operation(
            summary = "Обработка запроса на восстановление пароля",
            description = "Отправляет email с токеном восстановления"
    )
    @PostMapping("/reset-password")
    String processResetPassword(
            @Parameter(description = "Запрос на восстановление пароля")
            @Valid @ModelAttribute("request") ResetPasswordRequestDTO request,
            BindingResult bindingResult
    );

    @Operation(
            summary = "Форма смены пароля по токену",
            description = "Отображает страницу, на которой пользователь вводит новый пароль"
    )
    @GetMapping("/reset-form/{token}")
    String showResetPasswordUpdateForm(
            @Parameter(description = "Токен восстановления") @PathVariable String token,
            Model model
    );

    @Operation(
            summary = "Обновление пароля по токену",
            description = "Сохраняет новый пароль для пользователя, если токен валиден"
    )
    @PostMapping("/reset-form/{token}")
    String processResetPasswordForm(
            @Parameter(description = "Новый пароль и подтверждение")
            @ModelAttribute("passwordUpdate") @Valid PasswordUpdateDTO passwordUpdate,
            BindingResult bindingResult,
            @Parameter(description = "Токен восстановления") @PathVariable String token,
            @RequestParam String password,
            @RequestParam String passwordConfirm,
            Model model
    );

    @Operation(
            summary = "Профиль пользователя",
            description = "Отображает страницу профиля пользователя"
    )
    @GetMapping("/profile")
    String profilePage(
            @Parameter(description = "Данные пользователя")
            @ModelAttribute("user") UserDTO user
    );
}
