package org.chous.bets.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/tables")
public interface TablesControllerAPI {

    @GetMapping("/first-round")
    String getFirstRound(Model model);

    @GetMapping("/second-round")
    String getSecondRound(Model model);

    @GetMapping("/third-round")
    String getThirdRound(Model model);

    @GetMapping("/knockout-stage")
    String getKnockoutStage(Model model);

    @GetMapping("/stage-total")
    String getStagesCombinedScore(Model model);

    @GetMapping
    String getTournamentLeaderboard(Model model);
}
