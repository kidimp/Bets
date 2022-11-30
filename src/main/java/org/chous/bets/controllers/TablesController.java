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
    private final BetDAO betDAO;
    private final WinningTeamDAO winningTeamDAO;
//    private final UsersRepository usersRepository;
    private final List<Stage> stages;
    private final List<Round> rounds;
    private final List<Team> teams;
    private final List<User> users;
    private final List<MatchView> matchViews;
    private final MatchDAO matchDAO;

    private class TableView {
        public List<TableRow> tableRows;
        public List<MatchView> matchViews;

        TableView(List<TableRow> tableRows, List<MatchView> matchViews) {
            this.tableRows = tableRows;
            this.matchViews = matchViews;
        }

        public List<TableRow> getTableRows() {
            return tableRows;
        }

        public List<MatchView> getMatchViews() {
            return matchViews;
        }
    }


    @Autowired
    public TablesController(MatchDAO matchDAO, WinningTeamDAO winningTeamDAO, TeamDAO teamDAO, StageDAO stageDAO, RoundDAO roundDAO, BetDAO betDAO, UsersRepository usersRepository) {
        this.betDAO = betDAO;
        this.matchDAO = matchDAO;
        this.winningTeamDAO = winningTeamDAO;
//        this.usersRepository = usersRepository;
        stages = stageDAO.stages();
        rounds = roundDAO.rounds();
        teams = teamDAO.teams();
        users = usersRepository.findAll();
        matchViews = new ArrayList<>();

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

    // атрымліваем каманды-пераможцы
    public List<String> getWinningTeams() {
        List<String> winningTeams = new ArrayList<>();
        for (User user : users) {
            Integer winningTeamId = winningTeamDAO.showWinningTeamId(user.getId());
            if (winningTeamId != null) {
                for (Team team : teams) {
                    if (winningTeamId == team.getId()) {
                        winningTeams.add(team.getName());
                    }
                }
            } else {
                winningTeams.add("");
            }
        }
        return winningTeams;
    }


    public void setTableRows(List<TableRow> tableRows, List<Match> matchesByRound) {
        for (User user : users) {
            List<BetView> allUserBetsViews = getAllUserBetsViews(user, matchesByRound, teams);
            tableRows.add(new TableRow(user, allUserBetsViews, matchesByRound));
        }

        // sort by points
        for (int i = 0; i < tableRows.size(); i++) {
            tableRows.get(i).setPosition(getPosition(i, tableRows));
        }
    }


    private int getPosition(int num, List<TableRow> tableRows) {
        int position = 1;
        double totalPoint = tableRows.get(num).getTotalPointsForThisRound();
        for (TableRow row : tableRows) {
            if (row.getTotalPointsForThisRound() > totalPoint) {
                position++;
            }
        }
        return position;
    }


//    private List<Team> getWinningTeams() {
//        return winningTeamDAO.teams();
//    }


    public void setMatchViewsForTableHead(List<Match> matchesByRound) {
        for (Match match : matchesByRound) {
            matchViews.add(new MatchView(match, stages, rounds, teams));
        }
    }


    private TableView setupTableView(int numberOfRound) {
        List<Match> matches = matchDAO.matches();
        List<TableRow> tableRows = new ArrayList<>();

        List<Match> matchesByRound = (numberOfRound != 0) ?
                MatchService.getMatchesByRound(numberOfRound, matches) : matches;
        matchesByRound.sort(Match.COMPARE_BY_DATE);
        setTableRows(tableRows, matchesByRound);

        matchViews.clear();
        setMatchViewsForTableHead(matchesByRound);

        return new TableView(tableRows, matchViews);
    }


    private void setupTable(int numberOfRound, Model model) {
        UserService.getCurrentPrincipalUserRole(model);

        TableView tableView = setupTableView(numberOfRound);

        model.addAttribute("tableRows", tableView.getTableRows());
        model.addAttribute("matchViews", tableView.getMatchViews());
    }


    @GetMapping("/tables/first_round")
    public String tableFirstRound(Model model) {
        setupTable(1, model);
        return "tables/first_round";
    }


    @GetMapping("/tables/second_round")
    public String tableSecondRound(Model model) {
        setupTable(2, model);
        return "tables/second_round";
    }


    @GetMapping("/tables/third_round")
    public String tableThirdRound(Model model) {
        setupTable(3, model);
        return "tables/third_round";
    }


    @GetMapping("/tables/knockout_stage")
    public String tableKnockoutStage(Model model) {
        setupTable(4, model);
        return "tables/knockout_stage";
    }


    @GetMapping("/tables/whole_tournament")
    public String tableWholeTournament(Model model) {
        setupTable(0, model);
        model.addAttribute("winningTeams", getWinningTeams());
        return "tables/whole_tournament";
    }


    @GetMapping("/tables")
    public String tables(Model model) {
        final int AMOUNT_OF_STAGES = 5;
        TableView[] tableViews = new TableView[AMOUNT_OF_STAGES];

        //init table
        for (int i = 0; i < AMOUNT_OF_STAGES; i++) {
            tableViews[i] = setupTableView((i != 4) ? i + 1 : 0);
        }

        //calculate positions
        List<Double> positions = new ArrayList<>();
        for (int num = 0; num < tableViews[0].getTableRows().size(); num++) {
            double position = 0.0;
            for (int i = 0; i < AMOUNT_OF_STAGES; i++) {
                position += tableViews[i].getTableRows().get(num).getPosition();
            }
            positions.add(position / AMOUNT_OF_STAGES);
        }

//        // атрымліваем каманды-пераможцы
//        List<String> winningTeams = new ArrayList<>();
//        for (User user : users) {
//            Integer winningTeamId = winningTeamDAO.showWinningTeamId(user.getId());
//            if (winningTeamId != null) {
//                for (Team team : teams) {
//                    if (winningTeamId == team.getId()) {
//                        winningTeams.add(team.getName());
//                    }
//                }
//            } else {
//                winningTeams.add("");
//            }
//        }

        model.addAttribute("positions", positions);
        model.addAttribute("firstRoundTableRows", tableViews[0].getTableRows());
        model.addAttribute("secondRoundTableRows", tableViews[1].getTableRows());
        model.addAttribute("thirdRoundTableRows", tableViews[2].getTableRows());
        model.addAttribute("knockoutRoundTableRows", tableViews[3].getTableRows());
        model.addAttribute("wholeTournamentTableRows", tableViews[4].getTableRows());
        model.addAttribute("users", users);
        model.addAttribute("winningTeams", getWinningTeams());

        return "tables";
    }


//    class GeneralRows {
//        List<User> users;
//        double averagePosition;
//        int firstRoundPoints;
//        int secondRoundPoints;
//        int thirdRoundPoints;
//        int knockoutPoints;
//        int wholePoints;
//
//        GeneralRows(TableView[] tableViews, List<User> users) {
//            //this.users = users;
//        }
//    }
}
