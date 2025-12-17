package org.chous.bets.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.chous.bets.model.dto.WinningTeamTournamentDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Admin Winning Team", description = "Админские настройки победителя")
@RequestMapping("/admin/winning-team")
public interface WinningTeamAdminControllerAPI {

    @Operation(
            summary = "Страница админской настройки победителя турнира",
            description = "Возвращает страницу для установки победителя турнира администратором"
    )
    @GetMapping()
    String showTournamentWinningTeamSetting(Model model);

    @Operation(
            summary = "Сохранение победителя турнира",
            description = "Обрабатывает форму админской настройки победителя турнира"
    )
    @PostMapping()
    String submitTournamentWinningTeamSetting(
            @Parameter(description = "Данные для установки победителя турнира")
            @Valid @ModelAttribute("winningTeam") WinningTeamTournamentDTO dto,
            BindingResult result
    );
}
