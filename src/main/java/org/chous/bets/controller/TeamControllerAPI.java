package org.chous.bets.controller;

import jakarta.validation.Valid;
import org.chous.bets.model.dto.TeamDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin/teams")
public interface TeamControllerAPI {

    @GetMapping()
    String listTeams(Model model);

    @GetMapping("/new")
    String newTeamForm(Model model);

    @PostMapping("/new")
    String createTeam(@ModelAttribute("team") @Valid TeamDTO teamDTO, BindingResult bindingResult);

    @GetMapping("/{id}/edit")
    String editTeamForm(@PathVariable("id") int id, Model model);

    @PostMapping("/{id}/edit")
    String updateTeam(@PathVariable("id") int id,
                      @ModelAttribute("team") @Valid TeamDTO teamDTO,
                      BindingResult bindingResult);

    @PostMapping("/{id}/delete")
    String deleteTeam(@PathVariable("id") int id);
}
