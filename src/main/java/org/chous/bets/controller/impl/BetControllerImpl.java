package org.chous.bets.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chous.bets.controller.BetControllerAPI;
import org.chous.bets.mapper.BetMapper;
import org.chous.bets.model.dto.BetDTO;
import org.chous.bets.model.dto.MatchDTO;
import org.chous.bets.model.entity.Bet;
import org.chous.bets.service.BetServiceAPI;
import org.chous.bets.service.MatchServiceAPI;
import org.chous.bets.service.StageServiceAPI;
import org.chous.bets.service.TeamServiceAPI;
import org.chous.bets.service.UserServiceAPI;
import org.chous.bets.util.SecurityContextUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BetControllerImpl implements BetControllerAPI {

    private final MatchServiceAPI matchService;
    private final BetServiceAPI betService;
    private final UserServiceAPI userService;
    private final StageServiceAPI stageService;
    private final TeamServiceAPI teamService;
    private final BetMapper betMapper;

    @Override
    public String showBetForm(int matchId, Model model) {
        try {
            String email = SecurityContextUtil.getPrincipal();
            int userId = userService.getPrincipalUserId(email);

            MatchDTO matchDTO = matchService.getMatchDTO(matchId);

            String homeTeamName = teamService.findById(matchDTO.getHomeTeamId()).getName();
            String awayTeamName = teamService.findById(matchDTO.getAwayTeamId()).getName();

            String stageName = stageService.findById(matchDTO.getStageId()).getName();

            Bet bet = betService.getOrCreateBetForUserAndMatch(userId, matchId);
            BetDTO betDTO = betMapper.toDto(bet);

            model.addAttribute("bet", betDTO);
            model.addAttribute("match", matchDTO);
            model.addAttribute("stage", stageName);
            model.addAttribute("homeTeamName", homeTeamName);
            model.addAttribute("awayTeamName", awayTeamName);

            return "bet";
        } catch (ResponseStatusException e) {
            return "redirect:/fixtures";
        }
    }

    @Override
    public String makeBet(int matchId, BetDTO betDTO, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            showBetForm(matchId, model);
            return "bet";
        }

        try {
            String email = SecurityContextUtil.getPrincipal();
            int userId = userService.getPrincipalUserId(email);

            betService.saveOrUpdateBet(userId, matchId, betDTO);

            return "redirect:/fixtures";
        } catch (ResponseStatusException ex) {
            return "redirect:/fixtures";
        }
    }
}
