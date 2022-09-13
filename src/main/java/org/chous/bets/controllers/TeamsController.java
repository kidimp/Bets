package org.chous.bets.controllers;

import org.chous.bets.dao.TeamDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TeamsController {

    private final TeamDAO teamDAO;

    @Autowired
    public TeamsController(TeamDAO teamDAO) {
        this.teamDAO = teamDAO;
    }

    @GetMapping("/teams")
    public String teams(Model model) {
        model.addAttribute("teams", teamDAO.teams());
        return "teams";
    }

}
