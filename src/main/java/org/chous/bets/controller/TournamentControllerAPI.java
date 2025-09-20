package org.chous.bets.controller;

import jakarta.validation.Valid;
import org.chous.bets.model.dto.TournamentDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер для админки - создание, редактирование, удаление турниров
 */
@RequestMapping("/admin/tournaments")
public interface TournamentControllerAPI {

    @GetMapping
    String getAllTournaments(Model model);

    @GetMapping("/new")
    String showCreateTournamentForm(@ModelAttribute("tournament") TournamentDTO tournamentDTO, Model model);

    @PostMapping("/new")
    String createTournament(@ModelAttribute("tournament") @Valid TournamentDTO tournamentDTO, BindingResult result, Model model);

    @GetMapping("/{id}/edit")
    String showEditTournamentForm(@PathVariable("id") Integer id, Model model);

    @PostMapping("/{id}/edit")
    String updateTournament(@PathVariable("id") Integer id,
                            @ModelAttribute("tournament") @Valid TournamentDTO tournamentDTO,
                            BindingResult result,
                            Model model);

    @PostMapping("/{id}/delete")
    String deleteTournament(@PathVariable("id") Integer id);
}
