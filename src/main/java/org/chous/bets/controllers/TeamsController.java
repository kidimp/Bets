package org.chous.bets.controllers;

import org.chous.bets.dao.TeamDAO;
import org.chous.bets.models.Team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class TeamsController {

    private final TeamDAO teamDAO;

    @Autowired
    public TeamsController(TeamDAO teamDAO) {
        this.teamDAO = teamDAO;
    }


    @GetMapping("teams/all")
    public String teams(Model model) {
        model.addAttribute("teams", teamDAO.teams());
        return "teams/all";
    }


    @GetMapping("teams/new")
    public String newTeam(@ModelAttribute("team") Team team) {
        return "teams/new";
    }


    @PostMapping("/teams")
    public String create(@ModelAttribute("team") @Valid Team team, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "teams/new";
        }

        teamDAO.save(team);
        return "redirect:/teams/all";
    }


    @GetMapping("/teams/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("team", teamDAO.show(id));
        return "teams/edit";
    }


    @PostMapping("teams/{id}/edit")
    public String update(@ModelAttribute("team") @Valid Team team, BindingResult bindingResult,
                         @PathVariable("id") int id) {

        if (bindingResult.hasErrors()) {
            return "/teams/edit";
        }

        teamDAO.update(id, team);

        return "redirect:/teams/all";
    }


    @GetMapping("/teams/{id}/delete")
    public String delete(Model model, @PathVariable("id") int id) {
        model.addAttribute("team", teamDAO.show(id));
        return "teams/delete";
    }

    @PostMapping("teams/{id}/delete")
    public String deleteTeam(@PathVariable("id") int id) {
        teamDAO.delete(id);
        return "redirect:/teams/all";
    }
}
