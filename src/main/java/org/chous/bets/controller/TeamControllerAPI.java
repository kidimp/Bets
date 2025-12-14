package org.chous.bets.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.chous.bets.model.dto.TeamDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Admin Teams", description = "Админские операции для работы с командами")
@RequestMapping("/admin/teams")
public interface TeamControllerAPI {

    @Operation(
            summary = "Список всех команд",
            description = "Возвращает страницу со списком всех команд"
    )
    @GetMapping
    String getAllTeams(Model model);

    @Operation(
            summary = "Форма создания команды",
            description = "Возвращает страницу с формой для добавления новой команды"
    )
    @GetMapping("/new")
    String createTeamForm(
            @Parameter(description = "DTO новой команды")
            @ModelAttribute("team") TeamDTO teamDTO,
            Model model
    );

    @Operation(
            summary = "Создание команды",
            description = "Обрабатывает форму добавления команды. В случае ошибок валидации возвращает форму обратно."
    )
    @PostMapping("/new")
    String createTeam(
            @Parameter(description = "Данные новой команды")
            @ModelAttribute("team") @Valid TeamDTO teamDTO,
            BindingResult bindingResult
    );

    @Operation(
            summary = "Форма редактирования команды",
            description = "Отображает форму редактирования команды по ID"
    )
    @GetMapping("/{id}/edit")
    String editTeamForm(
            @Parameter(description = "ID команды")
            @PathVariable("id") Integer id,
            Model model
    );

    @Operation(
            summary = "Обновление команды",
            description = "Обрабатывает форму редактирования команды"
    )
    @PostMapping("/{id}/edit")
    String updateTeam(
            @Parameter(description = "ID команды для обновления")
            @PathVariable("id") Integer id,
            @Parameter(description = "Обновлённые данные команды")
            @ModelAttribute("team") @Valid TeamDTO teamDTO,
            BindingResult bindingResult
    );

    @Operation(
            summary = "Удаление команды",
            description = "Удаляет команду по ID"
    )
    @PostMapping("/{id}/delete")
    String deleteTeam(
            @Parameter(description = "ID команды для удаления")
            @PathVariable("id") Integer id
    );
}
