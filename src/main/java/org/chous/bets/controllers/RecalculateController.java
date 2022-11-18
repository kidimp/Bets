//package org.chous.bets.controllers;
//
//import org.chous.bets.dao.BetDAO;
//import org.chous.bets.dao.MatchDAO;
//import org.chous.bets.dao.TeamDAO;
//import org.chous.bets.models.*;
//import org.chous.bets.repositories.UsersRepository;
//import org.chous.bets.services.MatchService;
//import org.chous.bets.services.PointsService;
//import org.chous.bets.services.TeamService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//@Controller
//@PreAuthorize("hasRole('ROLE_ADMIN')")
//public class RecalculateController {
//    private final BetDAO betDAO;
//    private final MatchDAO matchDAO;
//    private final TeamDAO teamDAO;
//    private PointsService pointsService;
//    private final UsersRepository usersRepository;
//    private final List<TableRow> generalTableRows;
//
//
//    @Autowired
//    public RecalculateController(BetDAO betDAO, MatchDAO matchDAO, TeamDAO teamDAO, UsersRepository usersRepository) {
//        this.betDAO = betDAO;
//        this.matchDAO = matchDAO;
//        this.teamDAO = teamDAO;
//        this.usersRepository = usersRepository;
//        generalTableRows = new ArrayList<>();
//    }
//
//
//    @GetMapping("recalculate_tables")
//    public String recalculateTables() {
//        return "recalculate_tables";
//    }
//
//
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
//            generalTableRows.add(new TableRow(user, totalPointsFirstRound, totalPointsSecondRound,
//                    totalPointsThirdRound, totalPointsKnockoutRound, totalPointsWholeTournament));
//        }
//
//        return "redirect:/tables";
//    }
//
//
//    public double getTotalPointsByRound(User user, int round) {
//        List<Match> matches = matchDAO.matches();
//        double totalPointsByRound = 0;
//        for (Bet bet : betDAO.bets()) {
//            Match match = MatchService.getMatchById(bet.getMatchId(), matches);
//            if (match != null) {
//                if ((bet.getUserId() == user.getId()) && (match.getRound() == round)) {
//                    totalPointsByRound += bet.getPoints();
//                }
//            }
//        }
//        return totalPointsByRound;
//    }
//
//
//}