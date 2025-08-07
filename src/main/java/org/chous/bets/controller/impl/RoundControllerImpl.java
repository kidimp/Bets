package org.chous.bets.controller.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.controller.RoundControllerAPI;
import org.chous.bets.model.dto.RoundDTO;
import org.chous.bets.service.RoundServiceAPI;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RoundControllerImpl implements RoundControllerAPI {

    private final RoundServiceAPI roundService;

    @Override
    public String list(Model model) {
        model.addAttribute("rounds", roundService.findAll());
        return "rounds/all";
    }

    @Override
    public String createForm(RoundDTO round) {
        return "rounds/new";
    }

    @Override
    public String create(RoundDTO round, BindingResult result) {
        if (result.hasErrors()) {
            return "rounds/new";
        }
        roundService.save(round);
        return "redirect:/admin/rounds";
    }

    @Override
    public String editForm(Integer id, Model model) {
        model.addAttribute("round", roundService.findById(id));
        return "rounds/edit";
    }

    @Override
    public String update(Integer id, RoundDTO round, BindingResult result) {
        if (result.hasErrors()) {
            return "rounds/edit";
        }
        roundService.update(id, round);
        return "redirect:/admin/rounds";
    }

    @Override
    public String delete(Integer id) {
        roundService.delete(id);
        return "redirect:/admin/rounds";
    }
}
