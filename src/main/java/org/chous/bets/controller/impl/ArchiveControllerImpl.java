package org.chous.bets.controller.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.controller.ArchiveControllerAPI;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
public class ArchiveControllerImpl implements ArchiveControllerAPI {

    @Override
    public String getAllTournaments(Model model) {
        return "archive";
    }

    @Override
    public String getFirstRound(Model model) {
        return "archive/euro2024-first-round";
    }

    @Override
    public String getSecondRound(Model model) {
        return "archive/euro2024-second-round";
    }

    @Override
    public String getThirdRound(Model model) {
        return "archive/euro2024-third-round";
    }

    @Override
    public String getKnockoutStage(Model model) {
        return "archive/euro2024-knockout-stage";
    }

    @Override
    public String getStagesCombinedScore(Model model) {
        return "archive/euro2024-stage-total";
    }

    @Override
    public String getTournamentLeaderboard(Model model) {
        return "archive/euro2024-leaderboard";
    }


}
