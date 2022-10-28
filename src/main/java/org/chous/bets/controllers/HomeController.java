package org.chous.bets.controllers;

import org.chous.bets.dao.BetDAO;
import org.chous.bets.dao.MatchDAO;
import org.chous.bets.dao.StageDAO;
import org.chous.bets.dao.TeamDAO;
import org.chous.bets.models.*;

import org.chous.bets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final BetDAO betDAO;
    private final UsersRepository usersRepository;

    @Autowired
    public HomeController(MatchDAO matchDAO, TeamDAO teamDAO, StageDAO stageDAO, BetDAO betDAO, UsersRepository usersRepository) {
        this.matchDAO = matchDAO;
        this.teamDAO = teamDAO;
        this.stageDAO = stageDAO;
        this.betDAO = betDAO;
        this.usersRepository = usersRepository;
    }


    @ModelAttribute("teamsList")
    public List<Team> getTeamsList(Model model) {
        model.addAttribute("teams", teamDAO.teams());
        return teamDAO.teams();
    }


    @ModelAttribute("stagesList")
    public List<Stage> getStagesList(Model model) {
        model.addAttribute("stage", stageDAO.stages());
        return stageDAO.stages();
    }


    public void getUserRoleForHeaderVisualization(Model model) {
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


    public List<BetView> getAllUserBetsViews(User user) {
        List<Bet> allBets = betDAO.bets();
        List<BetView> usersBetsViewsArrayList = new ArrayList<>();
        for (Bet bet : allBets) {
            if (bet.getUserId() == user.getId()) {
                usersBetsViewsArrayList.add(new BetView(bet));
            }
        }
        return usersBetsViewsArrayList;
    }


    public List<BetView> getAllBetsViews() {
        List<Bet> allBets = betDAO.bets();
        List<BetView> allBetsViewsArrayList = new ArrayList<>();
        for (Bet bet : allBets) {
            allBetsViewsArrayList.add(new BetView(bet));
        }
        return allBetsViewsArrayList;
    }


    @GetMapping("/")
    public String home(Model model) {
        getUserRoleForHeaderVisualization(model);
        return "home";
    }

    @GetMapping("/fixtures")
    public String fixtures(Model model) {
        getUserRoleForHeaderVisualization(model);

        List<Match> matchesList = matchDAO.matches();
        matchesList.sort(Match.COMPARE_BY_DATE);

        List<Match> matchesListReversed = matchDAO.matches();
        matchesListReversed.sort(Collections.reverseOrder(Match.COMPARE_BY_DATE));

        ArrayList<MatchView> matchViewArrayList = new ArrayList<>();
        for (Match match : matchesList) {
            matchViewArrayList.add(new MatchView(match, stageDAO.stages(), teamDAO.teams()));
        }
        model.addAttribute("matchViews", matchViewArrayList);

        ArrayList<MatchView> matchViewArrayListReversed = new ArrayList<>();
        for (Match match : matchesListReversed) {
            matchViewArrayListReversed.add(new MatchView(match, stageDAO.stages(), teamDAO.teams()));
        }
        model.addAttribute("matchViewsReversed", matchViewArrayListReversed);

        model.addAttribute("betsViews", getAssumeBetsViewsForCurrentUser());

        return "fixtures";
    }

    @GetMapping("/tables")
    public String tables(Model model) {
        getUserRoleForHeaderVisualization(model);

        List<Match> matchesList = matchDAO.matches();
        matchesList.sort(Match.COMPARE_BY_DATE);

        ArrayList<MatchView> matchViewArrayList = new ArrayList<>();
        for (Match match : matchesList) {
            matchViewArrayList.add(new MatchView(match, stageDAO.stages(), teamDAO.teams()));
        }



        List<User> usersList = usersRepository.findAll();

        ArrayList<TableView> tableViewArrayList = new ArrayList<>();

        for (User user : usersList) {
            tableViewArrayList.add(new TableView(user, matchesList, getAllUserBetsViews(user)));
        }

        model.addAttribute("tablesViews", tableViewArrayList);




        model.addAttribute("matchViews", matchViewArrayList);
//        model.addAttribute("betsViews", getAllBetsViews());
//        model.addAttribute("matches", matchesList);
//        model.addAttribute("users", usersRepository.findAll());
//        model.addAttribute("bets", betDAO.bets());
        return "tables";
    }


    @GetMapping("/rules")
    public String rules(Model model) {
        getUserRoleForHeaderVisualization(model);
        return "rules";
    }


    @GetMapping("/bet/{matchId}")
    public String bet(Model model, @PathVariable("matchId") int matchId) {
        getUserRoleForHeaderVisualization(model);

        Match match = matchDAO.show(matchId);
        model.addAttribute("date", match.getDateInStr());
        model.addAttribute("stageName", stageDAO.show(match.getStageId()).getName());
        model.addAttribute("homeTeamName", teamDAO.show(match.getHomeTeamId()).getName());
        model.addAttribute("awayTeamName", teamDAO.show(match.getAwayTeamId()).getName());

        Bet bet = new Bet();

        bet.setMatchId(matchId);

        model.addAttribute("bet", bet);

        return "bet";
    }


    @PostMapping("/bet/{matchId}")
    public String makeBet(Model model, @ModelAttribute("bet") @Valid Bet bet, BindingResult bindingResult,
                          @PathVariable("matchId") int matchId) {
        getUserRoleForHeaderVisualization(model);

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
