package org.chous.bets.controller.admin;

import jakarta.validation.Valid;
import org.chous.bets.model.dto.UserUpdateDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admin Users", description = "Админские операции для управления пользователями")
@RequestMapping("/admin/users")
public interface UserControllerAPI {

    @Operation(
            summary = "Список пользователей",
            description = "Возвращает страницу со списком всех пользователей"
    )
    @GetMapping
    String getAllUsers(Model model);

    @Operation(
            summary = "Форма редактирования пользователя",
            description = "Возвращает страницу с формой редактирования пользователя по имени"
    )
    @GetMapping("/{username}/edit")
    String getEditForm(
            @Parameter(description = "Имя пользователя")
            @PathVariable("username") String username,
            Model model
    );

    @Operation(
            summary = "Обновление пользователя",
            description = "Обрабатывает форму редактирования пользователя. В случае ошибок валидации возвращает форму обратно."
    )
    @PostMapping("/{username}/edit")
    String updateUser(
            @Parameter(description = "Имя пользователя")
            @PathVariable("username") String username,
            @Parameter(description = "Данные для обновления пользователя")
            @Valid @ModelAttribute("user") UserUpdateDTO userUpdateDTO,
            BindingResult bindingResult,
            Model model
    );
}
