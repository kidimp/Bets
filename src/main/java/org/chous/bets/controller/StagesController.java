package org.chous.bets.controller;

import org.chous.bets.dao.StageDAO;
import org.chous.bets.model.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class StagesController {

    private final StageDAO stageDAO;


    @Autowired
    public StagesController(StageDAO stageDAO) {
        this.stageDAO = stageDAO;
    }


    @GetMapping("/admin/stages")
    public String stages(Model model) {

        model.addAttribute("stages", stageDAO.stages());
        return "stages/all";
    }


    @GetMapping("/admin/stages/new")
    public String newStage(@ModelAttribute("stage") Stage stage) {
        return "stages/new";
    }


    @PostMapping("/admin/stages/new")
    public String create(@ModelAttribute("stage") @Valid Stage stage, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "stages/new";
        }

        stageDAO.save(stage);
        return "redirect:/admin/stages";
    }


    @GetMapping("/admin/stages/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {

        model.addAttribute("stage", stageDAO.show(id));
        return "stages/edit";
    }


    @PostMapping("/admin/stages/{id}/edit")
    public String update(@ModelAttribute("stage") @Valid Stage stage, BindingResult bindingResult,
                         @PathVariable("id") int id) {

        if (bindingResult.hasErrors()) {
            return "/stages/edit";
        }

        stageDAO.update(id, stage);
        return "redirect:/admin/stages";
    }


    /*@GetMapping("/admin/stages/{id}/delete")
    public String delete(Model model, @PathVariable("id") int id) {

        model.addAttribute("stage", stageDAO.show(id));
        return "stages/delete";
    }*/


    @PostMapping("/admin/stages/{id}/delete")
    public String deleteStage(@PathVariable("id") int id) {

        stageDAO.delete(id);
        return "redirect:/admin/stages";
    }
}
