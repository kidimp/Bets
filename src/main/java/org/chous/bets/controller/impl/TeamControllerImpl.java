package org.chous.bets.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.chous.bets.controller.TeamControllerAPI;
import org.chous.bets.model.dto.TeamDTO;
import org.chous.bets.service.TeamServiceAPI;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class TeamControllerImpl implements TeamControllerAPI {

    private final TeamServiceAPI teamService;

    @Override
    public String listTeams(Model model) {
        model.addAttribute("teams", teamService.findAll());
        return "teams/all";
    }

    @Override
    public String newTeamForm(Model model) {
        model.addAttribute("team", new TeamDTO());
        return "teams/new";
    }

    @Override
    public String createTeam(@ModelAttribute("team") @Valid TeamDTO teamDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "teams/new";
        }
        teamService.create(teamDTO);
        return "redirect:/admin/teams";
    }

    @Override
    public String editTeamForm(@PathVariable("id") int id, Model model) {
        model.addAttribute("team", teamService.findById(id));
        return "teams/edit";
    }

    @Override
    public String updateTeam(@PathVariable("id") int id,
                             @ModelAttribute("team") @Valid TeamDTO teamDTO,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "teams/edit";
        }
        teamService.update(id, teamDTO);
        return "redirect:/admin/teams";
    }

    @Override
    public String deleteTeam(@PathVariable("id") int id) {
        teamService.delete(id);
        return "redirect:/admin/teams";
    }
}
