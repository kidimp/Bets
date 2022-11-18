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
import java.util.*;


@Controller
public class HomeController {
    private final MatchDAO matchDAO;
    private final TeamDAO teamDAO;
    private final StageDAO stageDAO;
    private final RoundDAO roundDAO;
    private final BetDAO betDAO;
    private final UsersRepository usersRepository;

    private PointsService pointsService;
    private final List<TableRow> generalTableRows;
    private final List<TableRow> generalTableRowsTmp;
    private final HashMap<User, Double> averagePositionByUser;

    @Autowired
    public HomeController(MatchDAO matchDAO, TeamDAO teamDAO, StageDAO stageDAO, RoundDAO roundDAO, BetDAO betDAO, UsersRepository usersRepository) {
        this.matchDAO = matchDAO;
        this.teamDAO = teamDAO;
        this.stageDAO = stageDAO;
        this.roundDAO = roundDAO;
        this.betDAO = betDAO;
        this.usersRepository = usersRepository;
        generalTableRows = new ArrayList<>();
        generalTableRowsTmp = new ArrayList<>();
        averagePositionByUser = new HashMap<>();
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


    @GetMapping("recalculate_tables")
    public String recalculateTables() {
        return "recalculate_tables";
    }


//    @PostMapping("recalculate_tables")
//    public String recalculate(Model model) {
//        List<Match> matches = matchDAO.matches();
//        List<Team> teams = teamDAO.teams();
//        List<User> users = usersRepository.findAll();
//
//        for (Bet bet : betDAO.bets()) {
//            Match match = MatchService.getMatchById(bet.getMatchId(), matches);
//
//            if (match != null) {
//                Team homeTeam = TeamService.getTeamById(match.getHomeTeamId(), teams);
//                Team awayTeam = TeamService.getTeamById(match.getAwayTeamId(), teams);
//                pointsService = new PointsService(bet, match, homeTeam, awayTeam);
//            }
//
//            double points = pointsService.getPointsForMatch();
//            bet.setPoints(points);
//
//            betDAO.updatePoints(bet.getId(), bet);
//        }
//
//        for (User user : users) {
//            double totalPointsFirstRound = getTotalPointsByRound(user, 1);
//            double totalPointsSecondRound = getTotalPointsByRound(user, 2);
//            double totalPointsThirdRound = getTotalPointsByRound(user, 3);
//            double totalPointsKnockoutRound = getTotalPointsByRound(user, 4);
//            double totalPointsWholeTournament = totalPointsFirstRound + totalPointsSecondRound +
//                    totalPointsThirdRound + totalPointsKnockoutRound;
//            generalTableRowsTmp.add(new TableRow(user, totalPointsFirstRound, totalPointsSecondRound,
//                    totalPointsThirdRound, totalPointsKnockoutRound, totalPointsWholeTournament));
//        }
//
////        for (TableRow tableRow : generalTableRowsTmp) {
////            setAveragePosition(tableRow);
////        }
//
//
//        setAveragePosition(generalTableRowsTmp);
//
//
//        for (Map.Entry<User, Double> entry : averagePositionByUser.entrySet()) {
//            for (TableRow tableRow : generalTableRowsTmp) {
//                if (tableRow.getUser() == entry.getKey()) {
//                    generalTableRows.add(new TableRow(tableRow.getUser(), entry.getValue(),
//                            tableRow.getTotalPointsFirstRound(), tableRow.getTotalPointsSecondRound(),
//                            tableRow.getTotalPointsThirdRound(), tableRow.getTotalPointsKnockoutRound(),
//                            tableRow.getTotalPointsWholeTournament()));
//                }
//            }
//        }
//
//        return "redirect:/tables";
//    }


    public double getTotalPointsByRound(User user, int round) {
        List<Match> matches = matchDAO.matches();
        double totalPointsByRound = 0;
        for (Bet bet : betDAO.bets()) {
            Match match = MatchService.getMatchById(bet.getMatchId(), matches);
            if (match != null) {
                if ((bet.getUserId() == user.getId()) && (match.getRound() == round)) {
                    totalPointsByRound += bet.getPoints();
                }
            }
        }
        return totalPointsByRound;
    }


//    public void setAveragePosition(List<TableRow> generalTableRowsTmp) {
//        TreeMap<Double, User> totalPointsForEachUserFirstRound = new TreeMap<>();
//        TreeMap<Double, User> totalPointsForEachUserSecondRound = new TreeMap<>();
//        TreeMap<Double, User> totalPointsForEachUserThirdRound = new TreeMap<>();
//        TreeMap<Double, User> totalPointsForEachUserKnockoutRound = new TreeMap<>();
//        TreeMap<Double, User> totalPointsForEachUserWholeTournament = new TreeMap<>();
//
//        for (TableRow tableRow : generalTableRowsTmp) {
//            totalPointsForEachUserFirstRound.put(tableRow.getTotalPointsFirstRound(), tableRow.getUser());
//            totalPointsForEachUserSecondRound.put(tableRow.getTotalPointsSecondRound(), tableRow.getUser());
//            totalPointsForEachUserThirdRound.put(tableRow.getTotalPointsThirdRound(), tableRow.getUser());
//            totalPointsForEachUserKnockoutRound.put(tableRow.getTotalPointsKnockoutRound(), tableRow.getUser());
//            totalPointsForEachUserWholeTournament.put(tableRow.getTotalPointsWholeTournament(), tableRow.getUser());
//        }
//
////        HashMap<User, ArrayList<Double>> averagePositionByRound = new HashMap<>();
//        ArrayList<ArrayList<Double>> averagePositionByRound = new ArrayList<>();
//
//        for (Map.Entry<Double, User> entry : totalPointsForEachUserFirstRound.entrySet()) {
//            double index = 0.0;
//            ArrayList<Double> arrayList = new ArrayList<>();
//            for (User user : usersRepository.findAll()) {
//                index++;
//                if (user == entry.getValue()) {
//                    arrayList.add(index);
//                    //averagePositionByRound.put(user, arrayList);
//                }
//            }
//
//        }
//
//        for (Map.Entry<Double, User> entry : totalPointsForEachUserSecondRound.entrySet()) {
//            double index = 0.0;
//            ArrayList<Double> arrayList = new ArrayList<>();
//            for (User user : usersRepository.findAll()) {
//                index++;
//                if (user == entry.getValue()) {
//                    arrayList.add(index);
//                    //averagePositionByRound.put(user, arrayList);
//                }
//            }
//        }
//
//        for (Map.Entry<Double, User> entry : totalPointsForEachUserThirdRound.entrySet()) {
//            double index = 0.0;
//            ArrayList<Double> arrayList = new ArrayList<>();
//            for (User user : usersRepository.findAll()) {
//                index++;
//                if (user == entry.getValue()) {
//                    arrayList.add(index);
//                    //averagePositionByRound.put(user, arrayList);
//                }
//            }
//        }
//
//        for (Map.Entry<Double, User> entry : totalPointsForEachUserKnockoutRound.entrySet()) {
//            double index = 0.0;
//            ArrayList<Double> arrayList = new ArrayList<>();
//            for (User user : usersRepository.findAll()) {
//                index++;
//                if (user == entry.getValue()) {
//                    arrayList.add(index);
//                    //averagePositionByRound.put(user, arrayList);
//                }
//            }
//        }
//
//        for (Map.Entry<Double, User> entry : totalPointsForEachUserWholeTournament.entrySet()) {
//            double index = 0.0;
//            ArrayList<Double> arrayList = new ArrayList<>();
//            for (User user : usersRepository.findAll()) {
//                index++;
//                if (user == entry.getValue()) {
//                    arrayList.add(index);
//                    //averagePositionByRound.put(user, arrayList);
//                }
//            }
//        }
//
//
//        /*for (Map.Entry<User, ArrayList<Double>> entry : averagePositionByRound.entrySet()) {
//            double pos = 0.0;
//            for (User user : usersRepository.findAll()) {
//                if (user == entry.getKey()) {
//                    for (Double i : entry.getValue())
//                        pos += i;
//                }
//            }
//            averagePositionByUser.put(entry.getKey(), pos);
//        }*/
//    }
}



