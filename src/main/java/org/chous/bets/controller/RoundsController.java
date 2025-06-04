package org.chous.bets.controller;

import org.chous.bets.dao.RoundDAO;

import org.chous.bets.model.Round;
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


    @GetMapping("/admin/rounds")
    public String rounds(Model model) {

        model.addAttribute("rounds", roundDAO.rounds());
        return "rounds/all";
    }


    @GetMapping("/admin/rounds/new")
    public String newRound(@ModelAttribute("round") Round round) {
        return "rounds/new";
    }


    @PostMapping("/admin/rounds/new")
    public String create(@ModelAttribute("round") @Valid Round round, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "rounds/new";
        }

        roundDAO.save(round);
        return "redirect:/admin/rounds";
    }


    @GetMapping("/admin/rounds/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {

        model.addAttribute("round", roundDAO.show(id));
        return "rounds/edit";
    }


    @PostMapping("/admin/rounds/{id}/edit")
    public String update(@ModelAttribute("round") @Valid Round round, BindingResult bindingResult,
                         @PathVariable("id") int id) {

        if (bindingResult.hasErrors()) {
            return "/rounds/edit";
        }

        roundDAO.update(id, round);
        return "redirect:/admin/rounds";
    }


    /*@GetMapping("/admin/rounds/{id}/delete")
    public String delete(Model model, @PathVariable("id") int id) {

        model.addAttribute("round", roundDAO.show(id));
        return "rounds/delete";
    }*/


    @PostMapping("/admin/rounds/{id}/delete")
    public String deleteRound(@PathVariable("id") int id) {

        roundDAO.delete(id);
        return "redirect:/admin/rounds";
    }
}
