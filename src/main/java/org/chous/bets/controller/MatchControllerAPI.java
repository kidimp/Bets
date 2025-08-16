package org.chous.bets.controller;

import jakarta.validation.Valid;
import org.chous.bets.model.dto.MatchDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// todo написать javadoc
/**
 * Контроллер для админки - создание, редактирование, удаление матчей
 */
@RequestMapping("/admin/matches")
public interface MatchControllerAPI {

    @GetMapping
    String getAllMatches(Model model);

    @GetMapping("/new")
    String showCreateMatchForm(@ModelAttribute("match") MatchDTO matchDTO, Model model);

    @PostMapping("/new")
    String createMatch(@ModelAttribute("match") @Valid MatchDTO matchDTO, BindingResult result, Model model);

    @GetMapping("/{id}/edit")
    String showEditMatchForm(@PathVariable("id") Integer id, Model model);

    @PostMapping("/{id}/edit")
    String updateMatch(@PathVariable("id") Integer id,
                       @ModelAttribute("match") @Valid MatchDTO matchDTO,
                       BindingResult result,
                       Model model);

    @PostMapping("/{id}/delete")
    String deleteMatch(@PathVariable("id") Integer id);
}
