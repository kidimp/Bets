package org.chous.bets.controller.admin.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.chous.bets.controller.admin.TeamControllerAPI;
import org.chous.bets.model.dto.TeamDTO;
import org.chous.bets.service.TeamService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class TeamControllerImpl implements TeamControllerAPI {

    private final TeamService teamService;

    @Override
    public String getAllTeams(Model model) {
        model.addAttribute("teams", teamService.findAll());
        return "teams/all";
    }

    @Override
    public String createTeamForm(@ModelAttribute("team") @Valid TeamDTO teamDTO, Model model) {
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
    public String editTeamForm(Integer id, Model model) {
        model.addAttribute("team", teamService.findById(id));
        return "teams/edit";
    }

    @Override
    public String updateTeam(Integer id, TeamDTO teamDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "teams/edit";
        }
        teamService.update(id, teamDTO);
        return "redirect:/admin/teams";
    }

    @Override
    public String deleteTeam(Integer id) {
        teamService.delete(id);
        return "redirect:/admin/teams";
    }
}
