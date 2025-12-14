package org.chous.bets.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.chous.bets.model.dto.MatchDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Admin Matches", description = "Админские операции: создание, редактирование, удаление матчей")
@RequestMapping("/admin/matches")
public interface MatchControllerAPI {

    @Operation(
            summary = "Получить список всех матчей",
            description = "Возвращает страницу со списком всех матчей"
    )
    @GetMapping
    String getAllMatches(Model model);

    @Operation(
            summary = "Показать форму создания матча",
            description = "Возвращает страницу с формой для создания нового матча"
    )
    @GetMapping("/new")
    String createMatchForm(
            @Parameter(description = "DTO матча")
            @ModelAttribute("match") MatchDTO matchDTO,
            Model model);

    @Operation(
            summary = "Создать новый матч",
            description = "Обрабатывает форму создания матча. В случае ошибок возвращает страницу формы."
    )
    @PostMapping("/new")
    String createMatch(
            @Parameter(description = "Данные нового матча")
            @ModelAttribute("match") @Valid MatchDTO matchDTO,
            BindingResult result,
            Model model);

    @Operation(
            summary = "Показать форму редактирования матча",
            description = "Отображает страницу редактирования матча по ID"
    )
    @GetMapping("/{id}/edit")
    String editMatchForm(
            @Parameter(description = "ID матча для редактирования")
            @PathVariable("id") Integer id,
            Model model);

    @Operation(
            summary = "Обновить матч",
            description = "Обрабатывает форму редактирования матча. Возвращает форму с ошибками или редирект."
    )
    @PostMapping("/{id}/edit")
    String updateMatch(
            @Parameter(description = "ID матча для обновления")
            @PathVariable("id") Integer id,
            @Parameter(description = "Обновлённые данные матча")
            @ModelAttribute("match") @Valid MatchDTO matchDTO,
            BindingResult result,
            Model model);

    @Operation(
            summary = "Удалить матч",
            description = "Удаляет матч по ID и выполняет редирект на список матчей"
    )
    @PostMapping("/{id}/delete")
    String deleteMatch(
            @Parameter(description = "ID матча для удаления")
            @PathVariable("id") Integer id);
}
