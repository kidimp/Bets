package org.chous.bets.controller;

import jakarta.validation.Valid;
import org.chous.bets.model.dto.BetDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/bet")
public interface BetControllerAPI {

    @GetMapping("/{matchId}")
    String showBetForm(@PathVariable("matchId") int matchId, Model model);

    @PostMapping("/{matchId}")
    String makeBet(@PathVariable int matchId, @ModelAttribute("bet") @Valid BetDTO betDTO, BindingResult bindingResult, Model model);
}
