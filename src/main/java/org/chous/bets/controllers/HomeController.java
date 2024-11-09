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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
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
        //UserService.getCurrentPrincipalUserRole(model);
        return "home";
    }


    @GetMapping("/fixtures")
    public String fixtures(Model model) {
        //UserService.getCurrentPrincipalUserRole(model);
        List<Stage> stageList = stageDAO.stages();
        List<Round> roundList = roundDAO.rounds();
        List<Team> teamList = teamDAO.teams();

        List<Match> matchesList = matchDAO.matches();
        matchesList.sort(Match.COMPARE_BY_DATE);

        List<Match> matchesListReversed = new ArrayList<>(matchesList);
        matchesListReversed.sort(Collections.reverseOrder(Match.COMPARE_BY_DATE));

        ArrayList<MatchView> matchViewArrayList = new ArrayList<>();
        for (Match match : matchesList) {
            matchViewArrayList.add(new MatchView(match, stageList, roundList, teamList));
        }
        model.addAttribute("matchViews", matchViewArrayList);

        ArrayList<MatchView> matchViewArrayListReversed = new ArrayList<>();
        for (Match match : matchesListReversed) {
            matchViewArrayListReversed.add(new MatchView(match, stageList, roundList, teamList));
        }
        model.addAttribute("matchViewsReversed", matchViewArrayListReversed);

        model.addAttribute("betsViews", getAssumeBetsViewsForCurrentUser());

        return "fixtures";
    }


    @GetMapping("/rules")
    public String rules(Model model) {
        //UserService.getCurrentPrincipalUserRole(model);
        return "rules";
    }

    private void prepareMatchView(Model model, int matchId) throws Exception {
        Match match = matchDAO.show(matchId);

        //Матча няма
        if (match == null) {
            throw new Exception();
        }

        //Праверка на немагчымасць ставак, калі гульня пачалася
        if (new Date().after(match.getDateAndTime())) {
            throw new Exception();
        }

        model.addAttribute("date", match.getDateInStr());
        model.addAttribute("round", match.getRound());
        model.addAttribute("stageName", stageDAO.show(match.getStageId()).getName());
        model.addAttribute("homeTeamName", teamDAO.show(match.getHomeTeamId()).getName());
        model.addAttribute("awayTeamName", teamDAO.show(match.getAwayTeamId()).getName());
    }

    @GetMapping("/bet/{matchId}")
    public String bet(Model model, @PathVariable("matchId") int matchId) {
        try{
            prepareMatchView(model, matchId);
        }
        catch (Exception ex) {
            // TODO: 404
            return "redirect:/fixtures";
        }

        Bet bet = betDAO.show(UserService.getCurrentPrincipalUserId(), matchId);
        if (bet == null) {
            bet = new Bet();
        }
        bet.setMatchId(matchId);

        model.addAttribute("bet", bet);

        return "/bet";
    }


    @PostMapping("/bet/{matchId}")
    public String makeBet(Model model, @ModelAttribute("bet") @Valid Bet bet, BindingResult bindingResult,
                          @PathVariable("matchId") int matchId) {
        if (bindingResult.hasErrors()) {
            try{
                prepareMatchView(model, matchId);
            }
            catch (Exception ex) {
                // TODO: 404
                return "redirect:/fixtures";
            }
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


//    @GetMapping("recalculate_tables")
//    public String recalculateTables() {
//        return "/WEB-INF/views/-recalculate-tables.html";
//    }


    @PostMapping("/admin/recalculate-tables")
    public String recalculate() {

        List<Match> matches = matchDAO.matches();
        List<Team> teams = teamDAO.teams();
        List<User> users = usersRepository.findAllActiveUsers();

        HashMap<Integer, Integer> userAndNumberOfHitsOnTheCorrectScoreMap = new HashMap<>();
        for (User user : users) {
            userAndNumberOfHitsOnTheCorrectScoreMap.put(user.getId(), 0);
        }
        HashMap<Integer, Integer> userAndNumberOfHitsOnTheMatchResultMap = new HashMap<>();
        for (User user : users) {
            userAndNumberOfHitsOnTheMatchResultMap.put(user.getId(), 0);
        }

        PointsService pointsService;
        for (Bet bet : betDAO.bets()) {
            double points = 0.0;
            Match match = MatchService.getMatchById(bet.getMatchId(), matches);

            if (match != null && match.isFinished()) {
                Team homeTeam = TeamService.getTeamById(match.getHomeTeamId(), teams);
                Team awayTeam = TeamService.getTeamById(match.getAwayTeamId(), teams);
                pointsService = new PointsService(bet, match, homeTeam, awayTeam);
                int userId = pointsService.getBet().getUserId();

                if ((userId == 57) && (match.getId() == 44)) {
                    int i = 0;
                }

                points = PointsService.round(pointsService.getPointsForMatch(), 2);



                if (pointsService.isHitOnTheCorrectScore()) {
                    if (userAndNumberOfHitsOnTheCorrectScoreMap.containsKey(userId)) {
                        int numberOfHitsOnTheCorrectScore = userAndNumberOfHitsOnTheCorrectScoreMap.get(userId) + 1;
                        userAndNumberOfHitsOnTheCorrectScoreMap.replace(userId, numberOfHitsOnTheCorrectScore);
                    } else {
                        userAndNumberOfHitsOnTheCorrectScoreMap.put(userId, 1);
                    }
                }

                if (userId == 26) {
                    int i = 0;
                }

                if (pointsService.isHitOnTheMatchResult()) {
                    if (userAndNumberOfHitsOnTheMatchResultMap.containsKey(userId)) {
                        int numberOfHitsOnTheMatchResult = userAndNumberOfHitsOnTheMatchResultMap.get(userId) + 1;
                        userAndNumberOfHitsOnTheMatchResultMap.replace(userId, numberOfHitsOnTheMatchResult);
                    } else {
                        userAndNumberOfHitsOnTheMatchResultMap.put(userId, 1);
                    }
                }
            }


            bet.setPoints(points);
            betDAO.updatePoints(bet.getId(), bet);

        }

        for (Map.Entry<Integer, Integer> entry : userAndNumberOfHitsOnTheCorrectScoreMap.entrySet()) {
            int userId = entry.getKey();
            int correctScore = entry.getValue();
            int matchResult = userAndNumberOfHitsOnTheMatchResultMap.get(userId);
            if (extraPointsDAO.showByUser(userId) == null) {
                extraPointsDAO.saveExtraPointsByUser(userId, new ExtraPoints());
            }
            extraPointsDAO.updateNumberOfHitsOnTheCorrectScoreAndNumberOfHitsOnTheMatchResult(userId, correctScore, matchResult);
        }


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

        return "redirect:/admin/matches";
    }


    @ModelAttribute("teamsList")
    public List<Team> getTeamsList(Model model) {
        model.addAttribute("teams", teamDAO.teams());
        return teamDAO.teams();
    }


    @GetMapping("winning-team")
    public String winningTeam(Model model, @ModelAttribute("winningTeam") ExtraPoints extraPoints) {
        // атрымліваем ролю бягучага карыстальніка для адлюстравання правільнай шапкі старонкі
        //UserService.getCurrentPrincipalUserRole(model);

        // атрымліваем id бягучага карыстальніка
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int currentPrincipalUserId = Objects.requireNonNull(usersRepository.findByUsername(authentication.getName())
                .orElse(null)).getId();

        // Атрымліваем дату і час, да якіх ёсць магчымасць зрабіць стаўку на каманду-пераможцу
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
        return "winning-team";
    }


    @PostMapping("winning-team")
    public String winningTeamPredict(@ModelAttribute("winningTeam") ExtraPoints extraPoints, BindingResult bindingResult) {

        if (bindingResult.hasErrors() || extraPoints.getWinningTeamId() == 0) {
            return "winning-team";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int currentPrincipalUserId = Objects.requireNonNull(usersRepository.findByUsername(authentication.getName())
                .orElse(null)).getId();

        if (extraPointsDAO.showByUser(currentPrincipalUserId) != null) {
            extraPointsDAO.updateWinningTeamByUser(currentPrincipalUserId, extraPoints.getWinningTeamId());
        } else {
            extraPointsDAO.saveExtraPointsByUser(currentPrincipalUserId, extraPoints);
        }

        return "redirect:/fixtures";
    }


    @GetMapping("/admin/winning-team")
    public String winningTeamSetting(Model model) {
        ExtraPoints extraPoints = extraPointsDAO.show();
        if (extraPoints == null) {
            extraPoints = new ExtraPoints();
        }
        model.addAttribute("extraPoints", extraPoints);
        return "/winning-team-setting";
    }


    @PostMapping("/admin/winning-team")
    public String winningTeamSet(@ModelAttribute("extraPoints") ExtraPoints extraPoints, BindingResult bindingResult) {

        if (bindingResult.hasErrors() || extraPoints.getDateAndTime() == null) {
            return "winning-team-setting";
        }

        if (extraPointsDAO.show() == null) {
            extraPointsDAO.saveWinningTeam(extraPoints);
        }

        extraPointsDAO.updateWinningTeam(extraPoints);

        return "redirect:/admin/matches";
    }
}