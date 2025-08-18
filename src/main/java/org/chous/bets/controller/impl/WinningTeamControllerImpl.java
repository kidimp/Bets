package org.chous.bets.controller.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.controller.WinningTeamControllerAPI;
import org.chous.bets.model.dto.TeamDTO;
import org.chous.bets.model.dto.WinningTeamPredictionDTO;
import org.chous.bets.model.dto.WinningTeamTournamentDTO;
import org.chous.bets.service.TeamServiceAPI;
import org.chous.bets.service.UserServiceAPI;
import org.chous.bets.service.WinningTeamServiceAPI;
import org.chous.bets.util.SecurityContextUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WinningTeamControllerImpl implements WinningTeamControllerAPI {

    private final WinningTeamServiceAPI winningTeamService;
    private final TeamServiceAPI teamService;
    private final UserServiceAPI userService;

    @ModelAttribute("teamsList")
    public List<TeamDTO> getTeamsList() {
        return teamService.findAll();
    }

    @Override
    public String showWinningTeamPredictionForm(Model model) {
        String email = SecurityContextUtil.getPrincipal();
        int userId = userService.getPrincipalUserId(email);

        Integer userWinningTeamIdPrediction = winningTeamService.getUserWinningTeamIdPrediction(userId);
        model.addAttribute("userWinningTeamIdPrediction", userWinningTeamIdPrediction);

        String userWinningTeamNamePrediction = winningTeamService.getUserWinningTeamNamePrediction(userId);
        model.addAttribute("userWinningTeamNamePrediction", userWinningTeamNamePrediction);

        String winningTeamPredictionDateAndTimeDeadline = winningTeamService.getWinningTeamPredictionDateAndTimeDeadline();
        model.addAttribute("winningTeamPredictionDateAndTimeDeadline", winningTeamPredictionDateAndTimeDeadline);

        boolean isPredictAvailable = winningTeamService.isWinningTeamPredictionAvailable();
        model.addAttribute("isPredictAvailable", isPredictAvailable);

        boolean isWinningTeamPredictionByUserSet = winningTeamService.isWinningTeamPredictionByUserSet(userId);
        model.addAttribute("isWinningTeamPredictionByUserSet", isWinningTeamPredictionByUserSet);

        return "winning-team";
    }

    @Override
    public String submitWinningTeamPrediction(@ModelAttribute("winningTeam") WinningTeamPredictionDTO dto, BindingResult result) {
        if (result.hasErrors() || dto.getWinningTeamId() == 0) {
            return "winning-team";
        }

        String email = SecurityContextUtil.getPrincipal();
        int userId = userService.getPrincipalUserId(email);

        winningTeamService.submitWinningTeamPrediction(userId, dto);
        return "redirect:/fixtures";
    }

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
