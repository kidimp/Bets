package org.chous.bets.controllers;

import org.chous.bets.dao.*;
import org.chous.bets.models.*;
import org.chous.bets.repositories.UsersRepository;
import org.chous.bets.services.MatchService;
import org.chous.bets.services.TeamService;
import org.chous.bets.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @GetMapping("/tables/first_round")
    public String tableFirstRound(Model model) {
        UserService.getCurrentPrincipalUserRole(model);

        List<TableRow> tableRows = new ArrayList<>();
        List<Stage> stages = stageDAO.stages();
        List<Round> rounds = roundDAO.rounds();
        List<Team> teams = teamDAO.teams();
        List<Match> matches = MatchService.getMatchesByRound(1, matchDAO.matches());

        matches.sort(Match.COMPARE_BY_DATE);

        for (User user : usersRepository.findAll()) {
            List<BetView> allUserBetsViews = getAllUserBetsViews(user, matches, teams);
            tableRows.add(new TableRow(user, allUserBetsViews, matches));
        }

        ArrayList<MatchView> matchViews = new ArrayList<>();
        for (Match match : matches) {
            matchViews.add(new MatchView(match, stages, rounds, teams));
        }

        for (TableRow tableRow : tableRows) {
            if (tableRow != null) {
                for (BetView betView : tableRow.getBetsViews()) {
                    if (betView != null) {
                        betView.calculatePoints();
                    }
                }
            }
        }

        model.addAttribute("tableRows", tableRows);
        model.addAttribute("matchViews", matchViews);

        return "tables/first_round";
    }


    @GetMapping("/tables/second_round")
    public String tableSecondRound(Model model) {
        UserService.getCurrentPrincipalUserRole(model);

        List<TableRow> tableRows = new ArrayList<>();

        List<Stage> stages = stageDAO.stages();
        List<Round> rounds = roundDAO.rounds();
        List<Team> teams = teamDAO.teams();
        List<Match> matches = MatchService.getMatchesByRound(2, matchDAO.matches());

        matches.sort(Match.COMPARE_BY_DATE);

        for (User user : usersRepository.findAll()) {
            List<BetView> allUserBetsViews = getAllUserBetsViews(user, matches, teams);
            tableRows.add(new TableRow(user, allUserBetsViews, matches));
        }

        ArrayList<MatchView> matchViews = new ArrayList<>();
        for (Match match : matches) {
            matchViews.add(new MatchView(match, stages, rounds, teams));
        }

        for (TableRow tableRow : tableRows) {
            if (tableRow != null) {
                for (BetView betView : tableRow.getBetsViews()) {
                    if (betView != null) {
                        betView.calculatePoints();
                    }
                }
            }
        }

        model.addAttribute("tableRows", tableRows);
        model.addAttribute("matchViews", matchViews);

        return "tables/second_round";
    }


    @GetMapping("/tables/third_round")
    public String tableThirdRound(Model model) {
        UserService.getCurrentPrincipalUserRole(model);

        List<TableRow> tableRows = new ArrayList<>();

        List<Stage> stages = stageDAO.stages();
        List<Round> rounds = roundDAO.rounds();
        List<Team> teams = teamDAO.teams();
        List<Match> matches = MatchService.getMatchesByRound(3, matchDAO.matches());

        matches.sort(Match.COMPARE_BY_DATE);

        for (User user : usersRepository.findAll()) {
            List<BetView> allUserBetsViews = getAllUserBetsViews(user, matches, teams);
            tableRows.add(new TableRow(user, allUserBetsViews, matches));
        }

        ArrayList<MatchView> matchViews = new ArrayList<>();
        for (Match match : matches) {
            matchViews.add(new MatchView(match, stages, rounds, teams));
        }

        for (TableRow tableRow : tableRows) {
            if (tableRow != null) {
                for (BetView betView : tableRow.getBetsViews()) {
                    if (betView != null) {
                        betView.calculatePoints();
                    }
                }
            }
        }

        model.addAttribute("tableRows", tableRows);
        model.addAttribute("matchViews", matchViews);

        return "tables/third_round";
    }


    @GetMapping("/tables/knockout_stage")
    public String tableKnockoutStage(Model model) {
        UserService.getCurrentPrincipalUserRole(model);

        List<TableRow> tableRows = new ArrayList<>();

        List<Stage> stages = stageDAO.stages();
        List<Round> rounds = roundDAO.rounds();
        List<Team> teams = teamDAO.teams();
        List<Match> matches = MatchService.getMatchesByRound(4, matchDAO.matches());

        matches.sort(Match.COMPARE_BY_DATE);

        for (User user : usersRepository.findAll()) {
            List<BetView> allUserBetsViews = getAllUserBetsViews(user, matches, teams);
            tableRows.add(new TableRow(user, allUserBetsViews, matches));
        }

        ArrayList<MatchView> matchViews = new ArrayList<>();
        for (Match match : matches) {
            matchViews.add(new MatchView(match, stages, rounds, teams));
        }

        for (TableRow tableRow : tableRows) {
            if (tableRow != null) {
                for (BetView betView : tableRow.getBetsViews()) {
                    if (betView != null) {
                        betView.calculatePoints();
                    }
                }
            }
        }

        model.addAttribute("tableRows", tableRows);
        model.addAttribute("matchViews", matchViews);

        return "tables/knockout_stage";
    }


    @GetMapping("/tables/whole_tournament")
    public String tableWholeTournament(Model model) {
        UserService.getCurrentPrincipalUserRole(model);

        List<TableRow> tableRows = new ArrayList<>();

        List<Stage> stages = stageDAO.stages();
        List<Round> rounds = roundDAO.rounds();
        List<Team> teams = teamDAO.teams();
        List<Match> matches =  matchDAO.matches();

        matches.sort(Match.COMPARE_BY_DATE);

        for (User user : usersRepository.findAll()) {
            List<BetView> allUserBetsViews = getAllUserBetsViews(user, matches, teams);
            tableRows.add(new TableRow(user, allUserBetsViews, matches));
        }

        ArrayList<MatchView> matchViews = new ArrayList<>();
        for (Match match : matches) {
            matchViews.add(new MatchView(match, stages, rounds, teams));
        }

        for (TableRow tableRow : tableRows) {
            if (tableRow != null) {
                for (BetView betView : tableRow.getBetsViews()) {
                    if (betView != null) {
                        betView.calculatePoints();
                    }
                }
            }
        }

        model.addAttribute("tableRows", tableRows);
        model.addAttribute("matchViews", matchViews);

        return "tables/whole_tournament";
    }
}
