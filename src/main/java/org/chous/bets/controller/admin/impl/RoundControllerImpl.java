package org.chous.bets.controller.admin.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.controller.admin.RoundControllerAPI;
import org.chous.bets.model.dto.RoundDTO;
import org.chous.bets.service.RoundService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RoundControllerImpl implements RoundControllerAPI {

    private final RoundService roundService;

    @Override
    public String getAllRounds(Model model) {
        model.addAttribute("rounds", roundService.findAll());
        return "rounds/all";
    }

    @Override
    public String createRoundForm(RoundDTO round) {
        return "rounds/new";
    }

    @Override
    public String createRound(RoundDTO round, BindingResult result) {
        if (result.hasErrors()) {
            return "rounds/new";
        }
        roundService.save(round);
        return "redirect:/admin/rounds";
    }

    @Override
    public String editRoundForm(Integer id, Model model) {
        model.addAttribute("round", roundService.findById(id));
        return "rounds/edit";
    }

    @Override
    public String updateRound(Integer id, RoundDTO round, BindingResult result) {
        if (result.hasErrors()) {
            return "rounds/edit";
        }
        roundService.update(id, round);
        return "redirect:/admin/rounds";
    }

    @Override
    public String deleteRound(Integer id) {
        roundService.delete(id);
        return "redirect:/admin/rounds";
    }
}
