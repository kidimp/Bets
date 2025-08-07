package org.chous.bets.controller;

import jakarta.validation.Valid;
import org.chous.bets.model.dto.RoundDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin/rounds")
public interface RoundControllerAPI {

    @GetMapping
    String list(Model model);

    @GetMapping("/new")
    String createForm(@ModelAttribute("round") RoundDTO round);

    @PostMapping("/new")
    String create(@ModelAttribute("round") @Valid RoundDTO round, BindingResult result);

    @GetMapping("/{id}/edit")
    String editForm(@PathVariable("id") Integer id, Model model);

    @PostMapping("/{id}/edit")
    String update(@PathVariable("id") Integer id, @ModelAttribute("round") @Valid RoundDTO round, BindingResult result);

    @PostMapping("/{id}/delete")
    String delete(@PathVariable("id") Integer id);
}
