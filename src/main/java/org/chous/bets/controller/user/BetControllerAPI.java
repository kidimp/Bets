package org.chous.bets.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.chous.bets.model.dto.BetDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Bet", description = "Создание ставок на матчи")
@RequestMapping("/bet")
public interface BetControllerAPI {

    @Operation(
            summary = "Показать форму ставки",
            description = "Возвращает страницу с формой для создания ставки на указанный матч"
    )
    @GetMapping("/{matchId}")
    String showBetForm(
            @Parameter(description = "ID матча, на который делается ставка")
            @PathVariable("matchId") int matchId,
            Model model
    );

    @Operation(
            summary = "Создать ставку",
            description = "Обрабатывает форму ставки. В случае ошибок валидации возвращает форму."
    )
    @PostMapping("/{matchId}")
    String makeBet(
            @Parameter(description = "ID матча, на который делается ставка")
            @PathVariable("matchId") int matchId,
            @Parameter(description = "Данные ставки")
            @ModelAttribute("bet") @Valid BetDTO betDTO,
            BindingResult bindingResult,
            Model model
    );
}
