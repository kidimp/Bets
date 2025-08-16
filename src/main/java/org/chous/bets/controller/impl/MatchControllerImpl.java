package org.chous.bets.controller.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.controller.MatchControllerAPI;
import org.chous.bets.model.dto.MatchDTO;
import org.chous.bets.service.MatchServiceAPI;
import org.chous.bets.service.RoundServiceAPI;
import org.chous.bets.service.StageServiceAPI;
import org.chous.bets.service.TeamServiceAPI;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class MatchControllerImpl implements MatchControllerAPI {

    private final MatchServiceAPI matchService;
    private final TeamServiceAPI teamService;
    private final StageServiceAPI stageService;
    private final RoundServiceAPI roundService;

    // todo эти данные очень редко будут меняться. Cacheable?
    private void populateReferenceData(Model model) {
        model.addAttribute("teamsList", teamService.findAll());
        model.addAttribute("stagesList", stageService.findAll());
        model.addAttribute("roundsList", roundService.findAll());
    }

    @Override
    public String getAllMatches(Model model) {
        List<MatchDTO> matches = matchService.findAllSortedByDateDesc();
        model.addAttribute("matches", matches);
        populateReferenceData(model);
        return "matches/all";
    }

    @Override
    public String showCreateMatchForm(MatchDTO matchDTO, Model model) {
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
    public String showEditMatchForm(Integer id, Model model) {
        MatchDTO matchDTO = matchService.findById(id);
        model.addAttribute("match", matchDTO);
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