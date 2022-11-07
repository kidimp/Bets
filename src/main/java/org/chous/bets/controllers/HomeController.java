package org.chous.bets.controllers;

import org.chous.bets.dao.*;
import org.chous.bets.models.*;

import org.chous.bets.repositories.UsersRepository;
import org.chous.bets.services.MatchService;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Controller
public class HomeController {
    private final MatchDAO matchDAO;
    private final TeamDAO teamDAO;
    private final StageDAO stageDAO;
    private final RoundDAO roundDAO;
    private final BetDAO betDAO;
    private final UsersRepository usersRepository;

    @Autowired
    public HomeController(MatchDAO matchDAO, TeamDAO teamDAO, StageDAO stageDAO, RoundDAO roundDAO, BetDAO betDAO, UsersRepository usersRepository) {
        this.matchDAO = matchDAO;
        this.teamDAO = teamDAO;
        this.stageDAO = stageDAO;
        this.roundDAO = roundDAO;
        this.betDAO = betDAO;
        this.usersRepository = usersRepository;
    }

//    @ModelAttribute("teamsList")
//    public List<Team> getTeamsList(Model model) {
//        model.addAttribute("teams", teamDAO.teams());
//        return teamDAO.teams();
//    }
//
//
//    @ModelAttribute("stagesList")
//    public List<Stage> getStagesList(Model model) {
//        model.addAttribute("stage", stageDAO.stages());
//        return stageDAO.stages();
//    }
//
//
//    public void getCurrentPrincipalUserRole(Model model) {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        String username;
//        if (principal instanceof UserDetails) {
//            username = ((UserDetails) principal).getUsername();
//        } else {
//            username = principal.toString();
//        }
//
//        if (usersRepository.findByUsername(username).isPresent()) {
//            model.addAttribute("role", usersRepository.findByUsername(username).get().getRole());
//        } else {
//            model.addAttribute("role", "ROLE_USER");
//        }
//    }
//
//
//    public int getCurrentPrincipalUserId() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return Objects.requireNonNull(usersRepository.findByUsername(authentication.getName())
//                .orElse(null)).getId();
//    }
//
//
//    public Match getMatchById(int matchId, List<Match> matches) {
//        for (Match match : matches) {
//            if (matchId == match.getId()) {
//                return match;
////                round = match.getRound();
//            }
//        }
//        return null;
//    }
//
//
//    public Team getTeamById(int teamId, List<Team> teams) {
//        for (Team team : teams) {
//            if (teamId == team.getId()) {
//                return team;
//            }
//        }
//        return null;
//    }




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


    // Атрымліваем для кожнага карыстальніка спіс яго ставак незалежна ад аўтэнтыфікацыі.
    public List<BetView> getAllUserBetsViews(User user, List<Match> matches, List<Team> teams) {
        List<Bet> allBets = betDAO.bets();
        List<BetView> usersBetsViews = new ArrayList<>();
        for (Bet bet : allBets) {
            if (bet.getUserId() == user.getId()) {
                Match match = MatchService.getMatchById(bet.getMatchId(), matches);
                if (match != null) {
                    Team homeTeam = TeamService.getTeamById(match.getHomeTeamId(), teams);
                    Team awayTeam = TeamService.getTeamById(match.getAwayTeamId(), teams);

                    usersBetsViews.add(new BetView(bet, match, homeTeam, awayTeam));
                }
            }
        }
        return usersBetsViews;
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


    @GetMapping("/tables")
    public String tables(Model model) {
        UserService.getCurrentPrincipalUserRole(model);

        List<TableRow> tableRows = new ArrayList<>();
        List<Match> matches = matchDAO.matches();
        List<Team> teams = teamDAO.teams();
        double totalPoints = 0.0;
        List<Match> matchesFirstRound = MatchService.getMatchesByRound(1, matches);
        List<Match> matchesSecondRound = MatchService.getMatchesByRound(2, matches);
        List<Match> matchesThirdRound = MatchService.getMatchesByRound(3, matches);
        List<Match> matchesKnockoutRound = MatchService.getMatchesByRound(4, matches);

        for (User user : usersRepository.findAll()) {
            List<BetView> userBetsViewsFirstRound = getAllUserBetsViews(user, matchesFirstRound, teams);
            List<BetView> userBetsViewsSecondRound = getAllUserBetsViews(user, matchesSecondRound, teams);
            List<BetView> userBetsViewsThirdRound = getAllUserBetsViews(user, matchesThirdRound, teams);
            List<BetView> userBetsViewsKnockoutRound = getAllUserBetsViews(user, matchesKnockoutRound, teams);

            tableRows.add(new TableRow(user, userBetsViewsFirstRound, userBetsViewsSecondRound, userBetsViewsThirdRound, userBetsViewsKnockoutRound));
        }

        model.addAttribute("tableRows", tableRows);

        return "tables";
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
    public String makeBet(Model model, @ModelAttribute("bet") @Valid Bet bet, BindingResult bindingResult,
                          @PathVariable("matchId") int matchId) {
        UserService.getCurrentPrincipalUserRole(model);

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
}
