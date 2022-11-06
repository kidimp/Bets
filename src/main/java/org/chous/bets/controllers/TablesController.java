package org.chous.bets.controllers;

import org.chous.bets.dao.*;
import org.chous.bets.models.*;
import org.chous.bets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TablesController {
    private final MatchDAO matchDAO;
    private final TeamDAO teamDAO;
    private final StageDAO stageDAO;
    private final RoundDAO roundDAO;
    private final BetDAO betDAO;
    private final UsersRepository usersRepository;

    @Autowired
    public TablesController(MatchDAO matchDAO, TeamDAO teamDAO, StageDAO stageDAO, RoundDAO roundDAO, BetDAO betDAO, UsersRepository usersRepository) {
        this.matchDAO = matchDAO;
        this.teamDAO = teamDAO;
        this.stageDAO = stageDAO;
        this.roundDAO = roundDAO;
        this.betDAO = betDAO;
        this.usersRepository = usersRepository;
    }


    public void getCurrentPrincipalUserRole(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        if (usersRepository.findByUsername(username).isPresent()) {
            model.addAttribute("role", usersRepository.findByUsername(username).get().getRole());
        } else {
            model.addAttribute("role", "ROLE_USER");
        }
    }


    // Атрымліваем для кожнага карыстальніка спіс яго ставак незалежна ад аўтэнтыфікацыі.
    public List<BetView> getAllUserBetsViews(User user, List<Match> matches, List<Team> teams) {
        List<Bet> allBets = betDAO.bets();
        List<BetView> usersBetsViews = new ArrayList<>();
        for (Bet bet : allBets) {
            if (bet.getUserId() == user.getId()) {
                usersBetsViews.add(new BetView(bet, matches, teams));
            }
        }
        return usersBetsViews;
    }





    @GetMapping("/results/first_round")
    public String tableFirstRound(Model model) {
        getCurrentPrincipalUserRole(model);

        List<UserBet> userBets = new ArrayList<>();
        List<Stage> stages = stageDAO.stages();
        List<Round> rounds = roundDAO.rounds();
        List<Team> teams = teamDAO.teams();
        List<Match> matches = new ArrayList<>();

        for (Match match : matchDAO.matches()) {
            if (match.getRound() == 1) {
                matches.add(match);
            }
        }

        matches.sort(Match.COMPARE_BY_DATE);

        for (User user : usersRepository.findAll()) {
            List<BetView> allUserBetsViews = getAllUserBetsViews(user, matches, teams);
            userBets.add(new UserBet(user, allUserBetsViews, matches));
        }

        ArrayList<MatchView> matchViews = new ArrayList<>();
        for (Match match : matches) {
            matchViews.add(new MatchView(match, stages, rounds, teams));
        }

        for (UserBet userBet : userBets) {
            if (userBet != null) {
                for (BetView betView : userBet.getBetsViews()) {
                    if (betView != null) {
                        betView.calculatePoints();
                    }
                }
            }
        }

        model.addAttribute("userBetsList", userBets);
        model.addAttribute("matchViews", matchViews);

        return "results/first_round";
    }


    @GetMapping("/results/second_round")
    public String tableSecondRound(Model model) {
        getCurrentPrincipalUserRole(model);

        List<UserBet> userBets = new ArrayList<>();

        List<Stage> stages = stageDAO.stages();
        List<Round> rounds = roundDAO.rounds();
        List<Team> teams = teamDAO.teams();
        List<Match> matches = new ArrayList<>();

        for (Match match : matchDAO.matches()) {
            if (match.getRound() == 2) {
                matches.add(match);
            }
        }

        matches.sort(Match.COMPARE_BY_DATE);

        for (User user : usersRepository.findAll()) {
            List<BetView> allUserBetsViews = getAllUserBetsViews(user, matches, teams);
            userBets.add(new UserBet(user, allUserBetsViews, matches));
        }

        ArrayList<MatchView> matchViews = new ArrayList<>();
        for (Match match : matches) {
            matchViews.add(new MatchView(match, stages, rounds, teams));
        }

        for (UserBet userBet : userBets) {
            if (userBet != null) {
                for (BetView betView : userBet.getBetsViews()) {
                    if (betView != null) {
                        betView.calculatePoints();
                    }
                }
            }
        }

        model.addAttribute("userBetsList", userBets);
        model.addAttribute("matchViews", matchViews);

        return "results/second_round";
    }


    @GetMapping("/results/third_round")
    public String tableThirdRound(Model model) {
        getCurrentPrincipalUserRole(model);

        List<UserBet> userBets = new ArrayList<>();

        List<Stage> stages = stageDAO.stages();
        List<Round> rounds = roundDAO.rounds();
        List<Team> teams = teamDAO.teams();
        List<Match> matches = new ArrayList<>();

        for (Match match : matchDAO.matches()) {
            if (match.getRound() == 3) {
                matches.add(match);
            }
        }

        matches.sort(Match.COMPARE_BY_DATE);

        for (User user : usersRepository.findAll()) {
            List<BetView> allUserBetsViews = getAllUserBetsViews(user, matches, teams);
            userBets.add(new UserBet(user, allUserBetsViews, matches));
        }

        ArrayList<MatchView> matchViews = new ArrayList<>();
        for (Match match : matches) {
            matchViews.add(new MatchView(match, stages, rounds, teams));
        }

        for (UserBet userBet : userBets) {
            if (userBet != null) {
                for (BetView betView : userBet.getBetsViews()) {
                    if (betView != null) {
                        betView.calculatePoints();
                    }
                }
            }
        }

        model.addAttribute("userBetsList", userBets);
        model.addAttribute("matchViews", matchViews);

        return "results/third_round";
    }


    @GetMapping("/results/knockout_stage")
    public String tableKnockoutStage(Model model) {
        getCurrentPrincipalUserRole(model);

        List<UserBet> userBets = new ArrayList<>();

        List<Stage> stages = stageDAO.stages();
        List<Round> rounds = roundDAO.rounds();
        List<Team> teams = teamDAO.teams();
        List<Match> matches = new ArrayList<>();

        for (Match match : matchDAO.matches()) {
            if (match.getRound() == 4) {
                matches.add(match);
            }
        }

        matches.sort(Match.COMPARE_BY_DATE);

        for (User user : usersRepository.findAll()) {
            List<BetView> allUserBetsViews = getAllUserBetsViews(user, matches, teams);
            userBets.add(new UserBet(user, allUserBetsViews, matches));
        }

        ArrayList<MatchView> matchViews = new ArrayList<>();
        for (Match match : matches) {
            matchViews.add(new MatchView(match, stages, rounds, teams));
        }

        for (UserBet userBet : userBets) {
            if (userBet != null) {
                for (BetView betView : userBet.getBetsViews()) {
                    if (betView != null) {
                        betView.calculatePoints();
                    }
                }
            }
        }

        model.addAttribute("userBetsList", userBets);
        model.addAttribute("matchViews", matchViews);

        return "results/knockout_stage";
    }
}
