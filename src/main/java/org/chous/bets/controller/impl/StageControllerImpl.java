package org.chous.bets.controller.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.controller.StageControllerAPI;
import org.chous.bets.model.dto.StageDTO;
import org.chous.bets.service.StageServiceAPI;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class StageControllerImpl implements StageControllerAPI {

    private final StageServiceAPI stageService;

    @Override
    public String findAll(Model model) {
        model.addAttribute("stages", stageService.findAll());
        return "stages/all";
    }

    @Override
    public String createForm(Model model) {
        model.addAttribute("stage", new StageDTO());
        return "stages/new";
    }

    @Override
    public String create(StageDTO stageDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "stages/new";
        }
        stageService.save(stageDTO);
        return "redirect:/admin/stages";
    }

    @Override
    public String editForm(Integer id, Model model) {
        model.addAttribute("stage", stageService.findById(id));
        return "stages/edit";
    }

    @Override
    public String update(Integer id, StageDTO stageDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "stages/edit";
        }
        stageService.update(id, stageDTO);
        return "redirect:/admin/stages";
    }

    @Override
    public String delete(Integer id) {
        stageService.delete(id);
        return "redirect:/admin/stages";
    }
}

