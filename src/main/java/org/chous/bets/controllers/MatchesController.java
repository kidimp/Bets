package org.chous.bets.controllers;

import org.chous.bets.dao.MatchDAO;
import org.chous.bets.dao.StageDAO;
import org.chous.bets.dao.TeamDAO;
import org.chous.bets.models.Match;
import org.chous.bets.models.Team;
import org.chous.bets.models.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class MatchesController {

    private final MatchDAO matchDAO;
    private final TeamDAO teamDAO;
    private final StageDAO stageDAO;

    @Autowired
    public MatchesController(MatchDAO matchDAO, TeamDAO teamDAO, StageDAO stageDAO) {
        this.matchDAO = matchDAO;
        this.teamDAO = teamDAO;
        this.stageDAO = stageDAO;
    }


    @ModelAttribute("teamsList")
    public List<Team> getTeamsList(Model model) {
        model.addAttribute("teams", teamDAO.teams());
        return teamDAO.teams();
    }


    @ModelAttribute("stagesList")
    public List<Stage> getStagesList(Model model) {
        model.addAttribute("stage", stageDAO.stages());
        return stageDAO.stages();
    }


    @GetMapping("matches/all")
    public String matches(Model model) {
        List<Match> matchesList = matchDAO.matches();
        Collections.sort(matchesList, Match.COMPARE_BY_DATE);

        model.addAttribute("matches", matchesList);
        model.addAttribute("teams", teamDAO.teams());
        model.addAttribute("stages", stageDAO.stages());
        return "matches/all";
    }


    @GetMapping("matches/new")
    public String newMatch(@ModelAttribute("match") Match match) {
        return "matches/new";
    }


    @PostMapping("/matches")
    public String create(@ModelAttribute("match") @Valid Match match, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "redirect:/matches/new";
        }

        matchDAO.save(match);
        return "redirect:/matches/all";
    }


    @GetMapping("/matches/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("match", matchDAO.show(id));
        return "matches/edit";
    }


    @PostMapping("matches/{id}/edit")
    public String update(@ModelAttribute("match") @Valid Match match, BindingResult bindingResult,
                         @PathVariable("id") int id) {

        if (bindingResult.hasErrors()) {
            return "/matches/edit";
        }

        try {
            matchDAO.update(id, match);
        } catch (Exception e) {
            matchDAO.delete(id);
        }

        return "redirect:/matches/all";
    }


    @GetMapping("/matches/{id}/delete")
    public String delete(Model model, @PathVariable("id") int id) {
        model.addAttribute("match", matchDAO.show(id));
        return "matches/delete";
    }

    @PostMapping("matches/{id}/delete")
    public String deleteMatch(@PathVariable("id") int id) {
        matchDAO.delete(id);
        return "redirect:/matches/all";
    }
}
