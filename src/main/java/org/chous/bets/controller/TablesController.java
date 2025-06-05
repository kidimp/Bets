package org.chous.bets.controller;

import org.chous.bets.dao.*;
import org.chous.bets.model.*;
import org.chous.bets.repository.UsersRepository;
import org.chous.bets.service.MatchService;
import org.chous.bets.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class TablesController {
    private final BetDAO betDAO;
    private final StageDAO stageDAO;
    private final TeamDAO teamDAO;
    private final RoundDAO roundDAO;
    private final ExtraPointsDAO extraPointsDAO;
    //private final List<Stage> stages;
    //private final List<Round> rounds;
    //private final List<Team> teams;

    private List<User> users;
    private final List<MatchView> matchViews;
    private final MatchDAO matchDAO;
    private final UsersRepository usersRepository;

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
    public TablesController(MatchDAO matchDAO, ExtraPointsDAO extraPointsDAO, TeamDAO teamDAO, StageDAO stageDAO, RoundDAO roundDAO, BetDAO betDAO, UsersRepository usersRepository) {
        this.betDAO = betDAO;
        this.matchDAO = matchDAO;
        this.stageDAO = stageDAO;
        this.teamDAO = teamDAO;
        this.roundDAO = roundDAO;
        this.extraPointsDAO = extraPointsDAO;
        this.usersRepository = usersRepository;
        /*stages = stageDAO.stages();
        rounds = roundDAO.rounds();
        teams = teamDAO.teams();
//        users = usersRepository.findAll();
        users = usersRepository.findAllActiveUsers();*/
        matchViews = new ArrayList<>();
    }


    // Атрымліваем для кожнага карыстальніка спіс яго ставак незалежна ад аўтэнтыфікацыі
    public List<BetView> getAllUserBetsViews(User user, List<Match> matches, List<Team> teams) {
        List<Bet> allUserBets = betDAO.betsByUser(user.getId());//betDAO.bets();
        List<BetView> usersBetsViews = new ArrayList<>();
        for (Bet bet : allUserBets) {
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

    // атрымліваем каманды-пераможцы для кожнага гульца
    public List<String> getWinningTeams() {
        List<Team> teams = teamDAO.teams();
        List<String> winningTeams = new ArrayList<>();

        for (User user : users) {
            Integer winningTeamId = extraPointsDAO.showWinningTeamIdByUser(user.getId());
            if (winningTeamId != null && winningTeamId != 0) {
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


    public List<Double> getExtraPoints() {
        List<Double> extraPointsList = new ArrayList<>();
        for (User user : users) {
            ExtraPoints extraPoints = extraPointsDAO.showByUser(user.getId());
            if (extraPoints != null) {
                double points = extraPoints.getExtraPoints();
                extraPointsList.add(points);
            } else {
                extraPointsList.add(0.0);
            }
        }
        return extraPointsList;
    }


    public List<Integer> getNumberOfHitsOnTheCorrectScore() {
        List<Integer> numberOfHitsOnTheCorrectScoreList = new ArrayList<>();
        for (User user : users) {
            ExtraPoints extraPoints = extraPointsDAO.showByUser(user.getId());
            if (extraPoints != null) {
                int numberOfHitsOnTheCorrectScore = extraPoints.getNumberOfHitsOnTheCorrectScore();
                numberOfHitsOnTheCorrectScoreList.add(numberOfHitsOnTheCorrectScore);
            } else {
                numberOfHitsOnTheCorrectScoreList.add(0);
            }
        }
        return numberOfHitsOnTheCorrectScoreList;
    }


    public List<Integer> getNumberOfHitsOnTheMatchResult() {
        List<Integer> numberOfHitsOnTheMatchResultList = new ArrayList<>();
        for (User user : users) {
            ExtraPoints extraPoints = extraPointsDAO.showByUser(user.getId());
            if (extraPoints != null) {
                int numberOfHitsOnTheMatchResult = extraPoints.getNumberOfHitsOnTheMatchResult();
                numberOfHitsOnTheMatchResultList.add(numberOfHitsOnTheMatchResult);
            } else {
                numberOfHitsOnTheMatchResultList.add(0);
            }
        }
        return numberOfHitsOnTheMatchResultList;
    }


    public void setTableRows(List<TableRow> tableRows, List<Match> matchesByRound, int roundIndex) {
        List<Team> teams = teamDAO.teams();

        for (User user : users) {
            double extraPoints = 0.0;
            if (roundIndex == 0) {
                try {
                    extraPoints = extraPointsDAO.showByUser(user.getId()).getExtraPoints();
                } catch (NullPointerException e) {
                }
            }
            List<BetView> allUserBetsViews = getAllUserBetsViews(user, matchesByRound, teams);
            tableRows.add(new TableRow(user, allUserBetsViews, matchesByRound, roundIndex, extraPoints));
        }

        // сартаваць па балах
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


    public void setMatchViewsForTableHead(List<Match> matchesByRound) {
        List<Stage> stages = stageDAO.stages();
        List<Round> rounds = roundDAO.rounds();
        List<Team> teams = teamDAO.teams();

        for (Match match : matchesByRound) {
            matchViews.add(new MatchView(match, stages, rounds, teams));
        }
    }


    private TableView setupTableView(int roundIndex) {
        List<Match> matches = matchDAO.matches();
        List<TableRow> tableRows = new ArrayList<>();

        List<Match> matchesByRound = (roundIndex != 0) ?
                matchDAO.matchesByRound(roundIndex) : matches;
                //MatchService.getMatchesByRound(roundIndex, matches) : matches;
        matchesByRound.sort(Match.COMPARE_BY_DATE);
        setTableRows(tableRows, matchesByRound, roundIndex);

        matchViews.clear();
        setMatchViewsForTableHead(matchesByRound);

        return new TableView(tableRows, matchViews);
    }


    private void setupTable(int numberOfRound, Model model) {
        //UserService.getCurrentPrincipalUserRole(model);

        users = usersRepository.findAllActiveUsers();

        TableView tableView = setupTableView(numberOfRound);

        model.addAttribute("numberOfRound", numberOfRound);
        model.addAttribute("tableRows", tableView.getTableRows());
        model.addAttribute("matchViews", tableView.getMatchViews());
    }


    @GetMapping("/tables/first-round")
    public String tableFirstRound(Model model) {
        setupTable(1, model);
        return "tables/stage-table";
    }


    @GetMapping("/tables/second-round")
    public String tableSecondRound(Model model) {
        setupTable(2, model);
        return "tables/stage-table";
    }


    @GetMapping("/tables/third-round")
    public String tableThirdRound(Model model) {
        setupTable(3, model);
        return "tables/stage-table";
    }


    @GetMapping("/tables/knockout-stage")
    public String tableKnockoutStage(Model model) {
        setupTable(4, model);
        return "tables/stage-table";
    }


    @GetMapping("/tables/whole-tournament")
    public String tableWholeTournament(Model model) {
        setupTable(0, model);
        model.addAttribute("winningTeams", getWinningTeams());
        model.addAttribute("extraPoints", getExtraPoints());
        model.addAttribute("numberOfHitsOnTheCorrectScore", getNumberOfHitsOnTheCorrectScore());
        model.addAttribute("numberOfHitsOnTheMatchResult", getNumberOfHitsOnTheMatchResult());

        boolean isPredictAvailable = (new Date()).before(extraPointsDAO.show().getDateAndTime());
        model.addAttribute("isPredictAvailable", isPredictAvailable);

        return "tables/whole-tournament";
    }


    @GetMapping("/tables")
    public String tables(Model model) {
        final int AMOUNT_OF_STAGES = 5;
        TableView[] tableViews = new TableView[AMOUNT_OF_STAGES];

        users = usersRepository.findAllActiveUsers();

        // ініцыялізуем табліцы
        for (int i = 0; i < AMOUNT_OF_STAGES; i++) {
            tableViews[i] = setupTableView((i != 4) ? i + 1 : 0);
        }

        // вылічаем пазіцыі
        List<Double> positions = new ArrayList<>();
        for (int num = 0; num < tableViews[0].getTableRows().size(); num++) {
            double position = 0.0;
            for (int i = 0; i < AMOUNT_OF_STAGES; i++) {
                position += tableViews[i].getTableRows().get(num).getPosition();
            }
            positions.add(position / AMOUNT_OF_STAGES);
        }

        boolean isPredictAvailable = (new Date()).before(extraPointsDAO.show().getDateAndTime());
        model.addAttribute("isPredictAvailable", isPredictAvailable);

        model.addAttribute("positions", positions);
        model.addAttribute("firstRoundTableRows", tableViews[0].getTableRows());
        model.addAttribute("secondRoundTableRows", tableViews[1].getTableRows());
        model.addAttribute("thirdRoundTableRows", tableViews[2].getTableRows());
        model.addAttribute("knockoutRoundTableRows", tableViews[3].getTableRows());
        model.addAttribute("wholeTournamentTableRows", tableViews[4].getTableRows());
        model.addAttribute("users", users);
        model.addAttribute("winningTeams", getWinningTeams());
        model.addAttribute("extraPoints", getExtraPoints());
        model.addAttribute("numberOfHitsOnTheCorrectScore", getNumberOfHitsOnTheCorrectScore());
        model.addAttribute("numberOfHitsOnTheMatchResult", getNumberOfHitsOnTheMatchResult());

        return "/tables/tables";
    }
}