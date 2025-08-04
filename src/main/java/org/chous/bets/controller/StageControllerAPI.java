package org.chous.bets.controller;

import jakarta.validation.Valid;
import org.chous.bets.model.dto.StageDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin/stages")
public interface StageControllerAPI {

    @GetMapping
    String findAll(Model model);

    @GetMapping("/new")
    String createForm(Model model);

    @PostMapping("/new")
    String create(@ModelAttribute("stage") @Valid StageDTO stageDTO, BindingResult bindingResult);

    @GetMapping("/{id}/edit")
    String editForm(@PathVariable("id") Integer id, Model model);

    @PostMapping("/{id}/edit")
    String update(@PathVariable("id") Integer id, @ModelAttribute("stage") @Valid StageDTO stageDTO, BindingResult bindingResult);

    @PostMapping("/{id}/delete")
    String delete(@PathVariable("id") Integer id);
}
