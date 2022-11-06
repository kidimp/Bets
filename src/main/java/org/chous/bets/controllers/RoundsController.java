package org.chous.bets.controllers;

import org.chous.bets.dao.RoundDAO;

import org.chous.bets.models.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class RoundsController {

    private final RoundDAO roundDAO;


    @Autowired
    public RoundsController(RoundDAO roundDAO) {
        this.roundDAO = roundDAO;
    }


    @GetMapping("rounds/all")
    public String rounds(Model model) {

        model.addAttribute("rounds", roundDAO.rounds());
        return "rounds/all";
    }


    @GetMapping("rounds/new")
    public String newRound(@ModelAttribute("round") Round round) {
        return "rounds/new";
    }


    @PostMapping("/rounds")
    public String create(@ModelAttribute("round") @Valid Round round, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "rounds/new";
        }

        roundDAO.save(round);
        return "redirect:/rounds/all";
    }


    @GetMapping("/rounds/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {

        model.addAttribute("round", roundDAO.show(id));
        return "rounds/edit";
    }


    @PostMapping("rounds/{id}/edit")
    public String update(@ModelAttribute("round") @Valid Round round, BindingResult bindingResult,
                         @PathVariable("id") int id) {

        if (bindingResult.hasErrors()) {
            return "/rounds/edit";
        }

        roundDAO.update(id, round);
        return "redirect:/rounds/all";
    }


    @GetMapping("/rounds/{id}/delete")
    public String delete(Model model, @PathVariable("id") int id) {

        model.addAttribute("round", roundDAO.show(id));
        return "rounds/delete";
    }


    @PostMapping("rounds/{id}/delete")
    public String deleteRound(@PathVariable("id") int id) {

        roundDAO.delete(id);
        return "redirect:/rounds/all";
    }
}
