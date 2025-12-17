package org.chous.bets.controller.admin.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.controller.admin.MatchControllerAPI;
import org.chous.bets.model.dto.MatchDTO;
import org.chous.bets.service.MatchService;
import org.chous.bets.service.impl.ReferenceDataService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class MatchControllerImpl implements MatchControllerAPI {

    private final MatchService matchService;
    private final ReferenceDataService referenceDataService;

    private void populateReferenceData(Model model) {
        ReferenceDataService.ReferenceData ref = referenceDataService.loadReferenceData();
        model.addAttribute("teamsList", ref.teams());
        model.addAttribute("stagesList", ref.stages());
        model.addAttribute("roundsList", ref.rounds());
    }

    @Override
    public String getAllMatches(Model model) {
        model.addAttribute("matches", matchService.findAllSortedByDateDesc());
        populateReferenceData(model);
        return "matches/all";
    }

    @Override
    public String createMatchForm(MatchDTO matchDTO, Model model) {
        populateReferenceData(model);
        return "matches/new";
    }

    @Override
    public String createMatch(MatchDTO matchDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            populateReferenceData(model);
            return "matches/new";
        }
        matchService.save(matchDTO);
        return "redirect:/admin/matches";
    }

    @Override
    public String editMatchForm(Integer id, Model model) {
        model.addAttribute("match", matchService.findById(id));
        populateReferenceData(model);
        return "matches/edit";
    }

    @Override
    public String updateMatch(Integer id, MatchDTO matchDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            populateReferenceData(model);
            return "matches/edit";
        }
        matchService.update(id, matchDTO);
        return "redirect:/admin/matches";
    }

    @Override
    public String deleteMatch(Integer id) {
        matchService.delete(id);
        return "redirect:/admin/matches";
    }
}