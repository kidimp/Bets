package org.chous.bets.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер для просмотра прошедших турниров
 */
@RequestMapping("/archive")
public interface ArchiveControllerAPI {

    @GetMapping
    String getAllTournaments(Model model);

    @GetMapping("/euro2024-first-round")
    String getFirstRound(Model model);

    @GetMapping("/euro2024-second-round")
    String getSecondRound(Model model);

    @GetMapping("/euro2024-third-round")
    String getThirdRound(Model model);

    @GetMapping("/euro2024-knockout-stage")
    String getKnockoutStage(Model model);

    @GetMapping("/euro2024-stage-total")
    String getStagesCombinedScore(Model model);

    @GetMapping("/euro2024-leaderboard")
    String getTournamentLeaderboard(Model model);
}
