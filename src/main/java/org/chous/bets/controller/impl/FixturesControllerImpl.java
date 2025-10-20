package org.chous.bets.controller.impl;

import lombok.AllArgsConstructor;
import org.chous.bets.controller.FixturesControllerAPI;
import org.chous.bets.service.BetService;
import org.chous.bets.service.MatchService;
import org.chous.bets.service.impl.ReferenceDataService;
import org.chous.bets.util.SecurityContextUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@AllArgsConstructor
public class FixturesControllerImpl implements FixturesControllerAPI {

    private final ReferenceDataService referenceDataService;
    private final MatchService matchService;
    private final BetService betService;

    @Override
    public String fixtures(Model model) {
        ReferenceDataService.ReferenceData ref = referenceDataService.loadReferenceData();
        model.addAttribute("teamsList", ref.teams());
        model.addAttribute("stagesList", ref.stages());
        model.addAttribute("roundsList", ref.rounds());

        model.addAttribute("upcomingAndStartedMatches", matchService.findAllUpcomingAndStartedMatches());
        model.addAttribute("finishedMatches", matchService.findAllFinishedMatches());
        if (SecurityContextUtil.isAuthenticated()) {
            model.addAttribute("bets", betService.getBets());
        }

        return "fixtures";
    }
}
