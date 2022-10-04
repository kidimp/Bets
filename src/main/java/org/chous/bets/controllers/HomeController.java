package org.chous.bets.controllers;

import org.chous.bets.dao.MatchDAO;
import org.chous.bets.dao.StageDAO;
import org.chous.bets.dao.TeamDAO;
import org.chous.bets.models.*;

import org.chous.bets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    private final MatchDAO matchDAO;
    private final TeamDAO teamDAO;
    private final StageDAO stageDAO;
    private final UsersRepository usersRepository;

    @Autowired
    public HomeController(MatchDAO matchDAO, TeamDAO teamDAO, StageDAO stageDAO, UsersRepository usersRepository) {
        this.matchDAO = matchDAO;
        this.teamDAO = teamDAO;
        this.stageDAO = stageDAO;
        this.usersRepository = usersRepository;
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

    @GetMapping("/")
    public String home(/*@PathVariable User user, Model model*/) {
//        model.addAttribute("user", user);
//        model.addAttribute("roles", Role.values());
        return "home";
    }

    @GetMapping("/fixtures")
    public String fixtures( Model model) {
        List<Match> matchesList = matchDAO.matches();
        Collections.sort(matchesList, Match.COMPARE_BY_DATE);

        List<Match> matchesListReversed = matchDAO.matches();
        Collections.sort(matchesListReversed, Collections.reverseOrder(Match.COMPARE_BY_DATE));

        model.addAttribute("matches", matchesList);
        model.addAttribute("matchesReversed", matchesListReversed);
        model.addAttribute("teams", teamDAO.teams());
        model.addAttribute("stages", stageDAO.stages());

        return "fixtures";
    }

    @GetMapping("/tables")
    public String tables() {
        return "tables";
    }


    @GetMapping("/bet")
    public String bet() {
        return "bet";
    }

}
