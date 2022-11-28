package org.chous.bets.controllers;

import org.chous.bets.dao.*;
import org.chous.bets.models.*;

import org.chous.bets.repositories.UsersRepository;
import org.chous.bets.services.MatchService;
import org.chous.bets.services.PointsService;
import org.chous.bets.services.TeamService;
import org.chous.bets.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
public class HomeController {
    private final MatchDAO matchDAO;
    private final TeamDAO teamDAO;
    private final StageDAO stageDAO;
    private final RoundDAO roundDAO;
    private final BetDAO betDAO;
    private final WinningTeamDAO winningTeamDAO;
    private final UsersRepository usersRepository;
    private PointsService pointsService;

    @Autowired
    public HomeController(MatchDAO matchDAO, TeamDAO teamDAO, StageDAO stageDAO, RoundDAO roundDAO, BetDAO betDAO,
                          WinningTeamDAO winningTeamDAO, UsersRepository usersRepository) {
        this.matchDAO = matchDAO;
        this.teamDAO = teamDAO;
        this.stageDAO = stageDAO;
        this.roundDAO = roundDAO;
        this.betDAO = betDAO;
        this.winningTeamDAO = winningTeamDAO;
        this.usersRepository = usersRepository;
    }


    // Атрымліваем спіс усіх ставак для аўтэнтыфікаванага карыстальніка.
    public List<BetView> getAssumeBetsViewsForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() != "anonymousUser") {
            int currentPrincipalUserId = Objects.requireNonNull(usersRepository.findByUsername(authentication.getName())
                    .orElse(null)).getId();
            List<Bet> allBets = betDAO.bets();
            List<BetView> usersBetsViewsArrayList = new ArrayList<>();
            for (Bet bet : allBets) {
                if (bet.getUserId() == currentPrincipalUserId) {
                    usersBetsViewsArrayList.add(new BetView(bet));
                }
            }
            return usersBetsViewsArrayList;
        }
        return null;
    }


    @GetMapping("/")
    public String home(Model model) {
        UserService.getCurrentPrincipalUserRole(model);
        return "home";
    }


    @GetMapping("/fixtures")
    public String fixtures(Model model) {
        UserService.getCurrentPrincipalUserRole(model);

        List<Match> matchesList = matchDAO.matches();
        matchesList.sort(Match.COMPARE_BY_DATE);

        List<Match> matchesListReversed = matchDAO.matches();
        matchesListReversed.sort(Collections.reverseOrder(Match.COMPARE_BY_DATE));

        ArrayList<MatchView> matchViewArrayList = new ArrayList<>();
        for (Match match : matchesList) {
            matchViewArrayList.add(new MatchView(match, stageDAO.stages(), roundDAO.rounds(), teamDAO.teams()));
        }
        model.addAttribute("matchViews", matchViewArrayList);

        ArrayList<MatchView> matchViewArrayListReversed = new ArrayList<>();
        for (Match match : matchesListReversed) {
            matchViewArrayListReversed.add(new MatchView(match, stageDAO.stages(), roundDAO.rounds(), teamDAO.teams()));
        }
        model.addAttribute("matchViewsReversed", matchViewArrayListReversed);

        model.addAttribute("betsViews", getAssumeBetsViewsForCurrentUser());

        return "fixtures";
    }


    @GetMapping("/rules")
    public String rules(Model model) {
        UserService.getCurrentPrincipalUserRole(model);
        return "rules";
    }


    @GetMapping("/bet/{matchId}")
    public String bet(Model model, @PathVariable("matchId") int matchId) {
        UserService.getCurrentPrincipalUserRole(model);

        Match match = matchDAO.show(matchId);
        model.addAttribute("date", match.getDateInStr());
        model.addAttribute("round", match.getRound());
        model.addAttribute("stageName", stageDAO.show(match.getStageId()).getName());
        model.addAttribute("homeTeamName", teamDAO.show(match.getHomeTeamId()).getName());
        model.addAttribute("awayTeamName", teamDAO.show(match.getAwayTeamId()).getName());

        Bet bet = betDAO.show(UserService.getCurrentPrincipalUserId(), matchId);
        if (bet == null) {
            bet = new Bet();
        }
        bet.setMatchId(matchId);

        model.addAttribute("bet", bet);

        return "bet";
    }


    @PostMapping("/bet/{matchId}")
    public String makeBet(@ModelAttribute("bet") @Valid Bet bet, BindingResult bindingResult,
                          @PathVariable("matchId") int matchId) {
        if (bindingResult.hasErrors()) {
            return "/bet";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int currentPrincipalUserId = Objects.requireNonNull(usersRepository.findByUsername(authentication.getName())
                .orElse(null)).getId();

        bet.setUserId(currentPrincipalUserId);

        List<Bet> allBets = betDAO.bets();
        for (Bet b : allBets) {
            if ((b.getUserId() == currentPrincipalUserId) && (b.getMatchId() == matchId)) {
                int id = b.getId();
                betDAO.update(id, bet);
                return "redirect:/fixtures";
            }
        }

        betDAO.save(bet);

        return "redirect:/fixtures";
    }


    @GetMapping("recalculate_tables")
    public String recalculateTables() {
        return "recalculate_tables";
    }


    @PostMapping("recalculate_tables")
    public String recalculate() {
        List<Match> matches = matchDAO.matches();
        List<Team> teams = teamDAO.teams();

        for (Bet bet : betDAO.bets()) {
            Match match = MatchService.getMatchById(bet.getMatchId(), matches);

            if (match != null) {
                Team homeTeam = TeamService.getTeamById(match.getHomeTeamId(), teams);
                Team awayTeam = TeamService.getTeamById(match.getAwayTeamId(), teams);
                pointsService = new PointsService(bet, match, homeTeam, awayTeam);
            }

            double points = pointsService.getPointsForMatch();
            bet.setPoints(points);
            betDAO.updatePoints(bet.getId(), bet);
        }

        return "redirect:/tables";
    }


    @ModelAttribute("teamsList")
    public List<Team> getTeamsList(Model model) {
        model.addAttribute("teams", teamDAO.teams());
        return teamDAO.teams();
    }


    @GetMapping("winning_team")
    public String winningTeam(Model model, @ModelAttribute("winningTeam") WinningTeam winningTeam) {
        UserService.getCurrentPrincipalUserRole(model);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int currentPrincipalUserId = Objects.requireNonNull(usersRepository.findByUsername(authentication.getName())
                .orElse(null)).getId();

        Date dateAndTime = winningTeamDAO.showWinningTeam().getDateAndTime();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String dateAndTimeInStr = df.format(dateAndTime);

        model.addAttribute("dateAndTime", dateAndTimeInStr);
        model.addAttribute("selectedByUser", winningTeamDAO.showWinningTeamId(currentPrincipalUserId));
        return "winning_team";
    }


    @PostMapping("winning_team")
    public String winningTeamPredict(@ModelAttribute("winningTeam") WinningTeam winningTeam, BindingResult bindingResult) {

        if (bindingResult.hasErrors() || winningTeam.getWinningTeamId() == 0) {
            return "/winning_team";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int currentPrincipalUserId = Objects.requireNonNull(usersRepository.findByUsername(authentication.getName())
                .orElse(null)).getId();

        if (winningTeamDAO.show(currentPrincipalUserId) != null) {
            winningTeamDAO.update(currentPrincipalUserId, winningTeam.getWinningTeamId());
            return "redirect:/tables";
        }

        winningTeamDAO.save(currentPrincipalUserId, winningTeam.getWinningTeamId());

        return "redirect:/tables";
    }


    @GetMapping("winning_team_setting")
    public String winningTeamSetting(Model model) {
        WinningTeam winningTeam = winningTeamDAO.showWinningTeam();
        if (winningTeam == null) {
            winningTeam = new WinningTeam();
        }
        model.addAttribute("winningTeam", winningTeam);
        return "winning_team_setting";
    }


    @PostMapping("winning_team_setting")
    public String winningTeamSet(@ModelAttribute("winningTeam") WinningTeam winningTeam, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/winning_team_setting";
        }

        if (winningTeamDAO.showWinningTeam() == null) {
            winningTeamDAO.saveWinningTeam(winningTeam);
        }

        winningTeamDAO.updateWinningTeam(winningTeam);

        return "redirect:/tables";
    }


}



