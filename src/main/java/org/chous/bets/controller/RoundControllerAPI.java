package org.chous.bets.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.chous.bets.model.dto.RoundDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Admin Rounds", description = "Админские операции с раундами турнира")
@RequestMapping("/admin/rounds")
public interface RoundControllerAPI {

    @Operation(
            summary = "Список раундов",
            description = "Возвращает страницу со списком всех раундов"
    )
    @GetMapping
    String getAllRounds(Model model);

    @Operation(
            summary = "Форма создания раунда",
            description = "Отображает форму для создания нового раунда"
    )
    @GetMapping("/new")
    String createRoundForm(
            @Parameter(description = "DTO нового раунда")
            @ModelAttribute("round") RoundDTO round
    );

    @Operation(
            summary = "Создание нового раунда",
            description = "Обрабатывает форму создания раунда. В случае ошибок валидации возвращает форму."
    )
    @PostMapping("/new")
    String createRound(
            @Parameter(description = "Данные создаваемого раунда")
            @ModelAttribute("round") @Valid RoundDTO round,
            BindingResult result
    );

    @Operation(
            summary = "Форма редактирования раунда",
            description = "Отображает форму редактирования раунда по его ID"
    )
    @GetMapping("/{id}/edit")
    String editRoundForm(
            @Parameter(description = "ID раунда")
            @PathVariable("id") Integer id,
            Model model
    );

    @Operation(
            summary = "Обновление раунда",
            description = "Обрабатывает форму редактирования раунда"
    )
    @PostMapping("/{id}/edit")
    String updateRound(
            @Parameter(description = "ID раунда для обновления")
            @PathVariable("id") Integer id,
            @Parameter(description = "Обновлённые данные раунда")
            @ModelAttribute("round") @Valid RoundDTO round,
            BindingResult result
    );

    @Operation(
            summary = "Удаление раунда",
            description = "Удаляет раунд по его ID"
    )
    @PostMapping("/{id}/delete")
    String deleteRound(
            @Parameter(description = "ID раунда для удаления")
            @PathVariable("id") Integer id
    );
}
