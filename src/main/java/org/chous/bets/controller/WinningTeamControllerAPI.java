package org.chous.bets.controller;

import org.chous.bets.model.dto.WinningTeamPredictionDTO;
import org.chous.bets.model.dto.WinningTeamTournamentDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

public interface WinningTeamControllerAPI {

    @GetMapping("/winning-team")
    String showWinningTeamPredictionForm(Model model);

    @PostMapping("/winning-team")
    String submitWinningTeamPrediction(@ModelAttribute("winningTeam") WinningTeamPredictionDTO dto, BindingResult result);

    @GetMapping("/admin/winning-team")
    String showTournamentWinningTeamSetting(Model model);

    @PostMapping("/admin/winning-team")
    String submitTournamentWinningTeamSetting(@ModelAttribute("winningTeam") WinningTeamTournamentDTO dto, BindingResult result);
}
