package org.chous.bets.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.chous.bets.model.dto.WinningTeamPredictionDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Winning Team", description = "Прогнозирование победителя турнира")
public interface WinningTeamControllerAPI {

    @Operation(
            summary = "Форма прогнозирования победителя",
            description = "Возвращает страницу с формой для предсказания победителя турнира пользователем"
    )
    @GetMapping("/winning-team")
    String showWinningTeamPredictionForm(Model model);

    @Operation(
            summary = "Отправка прогноза победителя",
            description = "Обрабатывает форму прогноза победителя турнира"
    )
    @PostMapping("/winning-team")
    String submitWinningTeamPrediction(
            @Parameter(description = "Данные прогноза победителя")
            @Valid @ModelAttribute("winningTeam") WinningTeamPredictionDTO dto,
            BindingResult result
    );
}
