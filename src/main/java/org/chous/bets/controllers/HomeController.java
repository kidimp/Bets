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
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
public class HomeController {
    private final MatchDAO matchDAO;
    private final TeamDAO teamDAO;
    private final StageDAO stageDAO;
    private final RoundDAO roundDAO;
    private final BetDAO betDAO;
    private final ExtraPointsDAO extraPointsDAO;
    private final UsersRepository usersRepository;
    private HashMap<Integer, Integer> userAndNumberOfHitsOnTheCorrectScoreMap = new HashMap<>();
    private HashMap<Integer, Integer> userAndNumberOfHitsOnTheMatchResultMap = new HashMap<>();
    int numberOfHitsOnTheCorrectScore = 0;
    int numberOfHitsOnTheMatchResult = 0;


    @Autowired
    public HomeController(MatchDAO matchDAO, TeamDAO teamDAO, StageDAO stageDAO, RoundDAO roundDAO, BetDAO betDAO,
                          ExtraPointsDAO extraPointsDAO, UsersRepository usersRepository) {
        this.matchDAO = matchDAO;
        this.teamDAO = teamDAO;
        this.stageDAO = stageDAO;
        this.roundDAO = roundDAO;
        this.betDAO = betDAO;
        this.extraPointsDAO = extraPointsDAO;
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

        Match match = MatchService.getMatchById(matchId, matchDAO.matches());

        List<Bet> allBets = betDAO.bets();
        for (Bet b : allBets) {
            if ((b.getUserId() == currentPrincipalUserId) && (b.getMatchId() == matchId)) {
                int id = b.getId();

                if (new Date().before(Objects.requireNonNull(match).getDateAndTime())) {
                    betDAO.update(id, bet);
                }
                return "redirect:/fixtures";
            }
        }

        if (new Date().before(Objects.requireNonNull(match).getDateAndTime())) {
            betDAO.save(bet);
        }

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
//        int numberOfHitsOnTheCorrectScore = 0;
//        int numberOfHitsOnTheMatchResult = 0;


        PointsService pointsService;
        for (Bet bet : betDAO.bets()) {
            double points = 0.0;
            Match match = MatchService.getMatchById(bet.getMatchId(), matches);

            if (match != null && match.isFinished()) {
                Team homeTeam = TeamService.getTeamById(match.getHomeTeamId(), teams);
                Team awayTeam = TeamService.getTeamById(match.getAwayTeamId(), teams);
                pointsService = new PointsService(bet, match, homeTeam, awayTeam);
                points = PointsService.round(pointsService.getPointsForMatch(), 2);

                int userId = pointsService.getBet().getUserId();

                if (pointsService.isHitOnTheCorrectScore()) {
                    if (userAndNumberOfHitsOnTheCorrectScoreMap.containsKey(userId)) {
                        numberOfHitsOnTheCorrectScore = userAndNumberOfHitsOnTheCorrectScoreMap.get(userId) + 1;
                        userAndNumberOfHitsOnTheCorrectScoreMap.replace(userId, numberOfHitsOnTheCorrectScore);
                    } else {
                        numberOfHitsOnTheCorrectScore = 1;
                        userAndNumberOfHitsOnTheCorrectScoreMap.put(userId, numberOfHitsOnTheCorrectScore);
                    }
                }

                if (pointsService.isHitOnTheMatchResult()) {
                    if (userAndNumberOfHitsOnTheMatchResultMap.containsKey(userId)) {
                        numberOfHitsOnTheMatchResult = userAndNumberOfHitsOnTheMatchResultMap.get(userId) + 1;
                        userAndNumberOfHitsOnTheMatchResultMap.replace(userId, numberOfHitsOnTheMatchResult);
                    } else {
                        numberOfHitsOnTheMatchResult = 1;
                        userAndNumberOfHitsOnTheMatchResultMap.put(userId, numberOfHitsOnTheMatchResult);
                    }
                }


//                if (pointsService.isHitOnTheCorrectScore() && pointsService.getBet().getUserId()) {
//                    numberOfHitsOnTheCorrectScore++;
//                }
//                if (pointsService.isHitOnTheMatchResult()) {
//                    numberOfHitsOnTheMatchResult++;
//                }
            }


            bet.setPoints(points);
            betDAO.updatePoints(bet.getId(), bet);

            if (extraPointsDAO.showByUser(bet.getUserId()) != null) {
                extraPointsDAO.updateNumberOfHitsOnTheCorrectScoreAndNumberOfHitsOnTheMatchResult(bet.getUserId(), numberOfHitsOnTheCorrectScore, numberOfHitsOnTheMatchResult);
            } else {
                extraPointsDAO.saveExtraPointsByUser(bet.getUserId(), new ExtraPoints());
                extraPointsDAO.updateNumberOfHitsOnTheCorrectScoreAndNumberOfHitsOnTheMatchResult(bet.getUserId(), numberOfHitsOnTheCorrectScore, numberOfHitsOnTheMatchResult);
            }
        }

        List<User> users = usersRepository.findAll();
        int winningTeamId = 0;
        int secondTeamId = 0;
        try {
            winningTeamId = extraPointsDAO.show().getWinningTeamId();
            secondTeamId = extraPointsDAO.show().getSecondPlaceTeamId();
        } catch (NullPointerException e) {
        }

        for (User user : users) {
            int winningTeamIdByUser;
            try {
                winningTeamIdByUser = extraPointsDAO.showWinningTeamIdByUser(user.getId());
            } catch (Exception e) {
                winningTeamIdByUser = 0;
            }
            pointsService = new PointsService(winningTeamId, secondTeamId, winningTeamIdByUser);
            int numberOfHitsOnTheCorrectScoreByUser = 0;
            int numberOfHitsOnTheMatchResultByUser = 0;
            try {
                // кожныя пяць трапленняў на дакладны счёт
                numberOfHitsOnTheCorrectScoreByUser = extraPointsDAO.showByUser(user.getId()).getNumberOfHitsOnTheCorrectScore() / 5;
                // кожныя пяць трапленняў на вынік матчу
                numberOfHitsOnTheMatchResultByUser = extraPointsDAO.showByUser(user.getId()).getNumberOfHitsOnTheMatchResult() / 5;
            } catch (NullPointerException e) {
            }
            double extraPoints = pointsService.getExtraPointsForWinningTeam() +
                    (double) (5 * numberOfHitsOnTheCorrectScoreByUser) +
                    (double) (3 * numberOfHitsOnTheMatchResultByUser);
            extraPointsDAO.updateExtraPointsByUser(user.getId(), extraPoints);
        }

        return "redirect:/tables";
    }


    @ModelAttribute("teamsList")
    public List<Team> getTeamsList(Model model) {
        model.addAttribute("teams", teamDAO.teams());
        return teamDAO.teams();
    }


    @GetMapping("winning_team")
    public String winningTeam(Model model, @ModelAttribute("winningTeam") ExtraPoints extraPoints) {
        // атрымліваем ролю бягучага карыстальніка для адлюстравання правільнай шапкі старонкі
        UserService.getCurrentPrincipalUserRole(model);

        // атрымліваем id бягучага карыстальніка
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int currentPrincipalUserId = Objects.requireNonNull(usersRepository.findByUsername(authentication.getName())
                .orElse(null)).getId();

        // Атрымліваем дату і час да якіх ёсць магчамасть зрабіть стаўку на каманду-пераможцу
        Date dateAndTime;
        try {
            dateAndTime = extraPointsDAO.show().getDateAndTime();
        } catch (NullPointerException e) {
            dateAndTime = new Date(1);
        }
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String dateAndTimeInStr = df.format(dateAndTime);
        Date currentDateAndTime = new Date();
        boolean isPredictAvailable = currentDateAndTime.before(dateAndTime);

        // Атрымліваем каманду-пераможцу на якую зрабіў стаўку гулец
        String winningTeamNameByUser = "";
        boolean isWinningTeamNameByUserSet = true;
        int winningTeamIdByUser;
        try {
            winningTeamIdByUser = extraPointsDAO.showWinningTeamIdByUser(currentPrincipalUserId);
        } catch (NullPointerException e) {
            winningTeamIdByUser = 0;
        }
        for (Team team : teamDAO.teams()) {
            if (winningTeamIdByUser == team.getId()) {
                winningTeamNameByUser = team.getName();
            }
        }
        if (winningTeamNameByUser.equals("")) {
            isWinningTeamNameByUserSet = false;
        }

        model.addAttribute("dateAndTime", dateAndTimeInStr);
        model.addAttribute("isPredictAvailable", isPredictAvailable);
        model.addAttribute("winningTeamIdByUser", extraPointsDAO.showWinningTeamIdByUser(currentPrincipalUserId));
        model.addAttribute("winningTeamNameByUser", winningTeamNameByUser);
        model.addAttribute("isWinningTeamNameByUserSet", isWinningTeamNameByUserSet);
        return "winning_team";
    }


    @PostMapping("winning_team")
    public String winningTeamPredict(@ModelAttribute("winningTeam") ExtraPoints extraPoints, BindingResult bindingResult) {

        if (bindingResult.hasErrors() || extraPoints.getWinningTeamId() == 0) {
            return "redirect:/winning_team";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int currentPrincipalUserId = Objects.requireNonNull(usersRepository.findByUsername(authentication.getName())
                .orElse(null)).getId();

        if (extraPointsDAO.showByUser(currentPrincipalUserId) != null) {
            extraPointsDAO.updateWinningTeamByUser(currentPrincipalUserId, extraPoints.getWinningTeamId());
            return "redirect:/tables";
        } else {
            extraPointsDAO.saveExtraPointsByUser(currentPrincipalUserId, extraPoints);
        }

        return "redirect:/tables";
    }


    @GetMapping("winning_team_setting")
    public String winningTeamSetting(Model model) {
        ExtraPoints extraPoints = extraPointsDAO.show();
        if (extraPoints == null) {
            extraPoints = new ExtraPoints();
        }
        model.addAttribute("extraPoints", extraPoints);
        return "winning_team_setting";
    }


    @PostMapping("winning_team_setting")
    public String winningTeamSet(@ModelAttribute("extraPoints") ExtraPoints extraPoints, BindingResult bindingResult) {

        if (bindingResult.hasErrors() || extraPoints.getDateAndTime() == null) {
            return "/winning_team_setting";
        }

        if (extraPointsDAO.show() == null) {
            extraPointsDAO.saveWinningTeam(extraPoints);
        }

        extraPointsDAO.updateWinningTeam(extraPoints);

        return "redirect:/tables";
    }


}