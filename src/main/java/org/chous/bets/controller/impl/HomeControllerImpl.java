package org.chous.bets.controller.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.controller.HomeControllerAPI;
import org.chous.bets.model.dto.BetDTO;
import org.chous.bets.model.dto.MatchDTO;
import org.chous.bets.service.BetServiceAPI;
import org.chous.bets.service.MatchServiceAPI;
import org.chous.bets.service.RoundServiceAPI;
import org.chous.bets.service.StageServiceAPI;
import org.chous.bets.service.TeamServiceAPI;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeControllerImpl implements HomeControllerAPI {

    private final MatchServiceAPI matchService;
    private final TeamServiceAPI teamService;
    private final StageServiceAPI stageService;
    private final RoundServiceAPI roundService;
    private final BetServiceAPI betService;

    // todo эти данные очень редко будут меняться. Cacheable?
    private void populateReferenceData(Model model) {
        model.addAttribute("teamsList", teamService.findAll());
        model.addAttribute("stagesList", stageService.findAll());
        model.addAttribute("roundsList", roundService.findAll());
    }

    @Override
    public String home(Model model) {
        return "home";
    }

    @Override
    public String fixtures(Model model) {
        populateReferenceData(model);
        List<MatchDTO> upcomingAndStartedMatches = matchService.findAllUpcomingAndStartedMatches();
        model.addAttribute("upcomingAndStartedMatches", upcomingAndStartedMatches);
        List<MatchDTO> finishedMatches = matchService.findAllFinishedMatches();
        model.addAttribute("finishedMatches", finishedMatches);
        List<BetDTO> bets = betService.getBets(); // todo для анонимного юзера не показывать ставки
        model.addAttribute("bets", bets);
        return "fixtures";
    }

    @Override
    public String rules(Model model) {
        return "rules";
    }
}
