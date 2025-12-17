package org.chous.bets.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.chous.bets.model.dto.TournamentDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Admin Tournaments", description = "Админские операции для управления турнирами")
@RequestMapping("/admin/tournaments")
public interface TournamentControllerAPI {

    @Operation(
            summary = "Список всех турниров",
            description = "Возвращает страницу со списком всех турниров"
    )
    @GetMapping
    String getAllTournaments(Model model);

    @Operation(
            summary = "Форма создания турнира",
            description = "Возвращает страницу с формой для создания нового турнира"
    )
    @GetMapping("/new")
    String createTournamentForm(
            @Parameter(description = "DTO нового турнира")
            @ModelAttribute("tournament") TournamentDTO tournamentDTO,
            Model model
    );

    @Operation(
            summary = "Создание турнира",
            description = "Обрабатывает форму создания турнира. В случае ошибок валидации возвращает форму обратно."
    )
    @PostMapping("/new")
    String createTournament(
            @Parameter(description = "Данные нового турнира")
            @ModelAttribute("tournament") @Valid TournamentDTO tournamentDTO,
            BindingResult result,
            Model model
    );

    @Operation(
            summary = "Форма редактирования турнира",
            description = "Отображает форму редактирования турнира по его ID"
    )
    @GetMapping("/{id}/edit")
    String editTournamentForm(
            @Parameter(description = "ID турнира")
            @PathVariable("id") Integer id,
            Model model
    );

    @Operation(
            summary = "Обновление турнира",
            description = "Обрабатывает форму редактирования турнира"
    )
    @PostMapping("/{id}/edit")
    String updateTournament(
            @Parameter(description = "ID турнира для обновления")
            @PathVariable("id") Integer id,
            @Parameter(description = "Обновлённые данные турнира")
            @ModelAttribute("tournament") @Valid TournamentDTO tournamentDTO,
            BindingResult result,
            Model model
    );

    @Operation(
            summary = "Удаление турнира",
            description = "Удаляет турнир по его ID"
    )
    @PostMapping("/{id}/delete")
    String deleteTournament(
            @Parameter(description = "ID турнира для удаления")
            @PathVariable("id") Integer id
    );
}
