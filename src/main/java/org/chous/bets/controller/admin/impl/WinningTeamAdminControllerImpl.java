package org.chous.bets.controller.admin.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.controller.admin.WinningTeamAdminControllerAPI;
import org.chous.bets.model.dto.WinningTeamTournamentDTO;
import org.chous.bets.service.WinningTeamService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class WinningTeamAdminControllerImpl implements WinningTeamAdminControllerAPI {

    private final WinningTeamService winningTeamService;

    @Override
    public String showTournamentWinningTeamSetting(Model model) {
        WinningTeamTournamentDTO winningTeam = winningTeamService.getTournamentWinningTeam();
        model.addAttribute("winningTeam", winningTeam);
        return "winning-team-setting";
    }

    @Override
    public String submitTournamentWinningTeamSetting(WinningTeamTournamentDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return "winning-team-setting";
        }
        winningTeamService.submitTournamentWinningTeam(dto);
        return "redirect:/admin/matches";
    }
}
