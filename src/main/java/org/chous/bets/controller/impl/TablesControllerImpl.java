package org.chous.bets.controller.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.controller.TablesControllerAPI;
import org.chous.bets.model.dto.LeaderboardTableRowDTO;
import org.chous.bets.model.dto.TableViewDTO;
import org.chous.bets.service.TableService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;

import static org.chous.bets.util.Constants.ALL_STAGES_COMBINED;
import static org.chous.bets.util.Constants.FIRST_ROUND;
import static org.chous.bets.util.Constants.KNOCKOUT_STAGE;
import static org.chous.bets.util.Constants.SECOND_ROUND;
import static org.chous.bets.util.Constants.THIRD_ROUND;

@Controller
@RequiredArgsConstructor
public class TablesControllerImpl implements TablesControllerAPI {

    private final TableService tablesService;

    @Override
    public String getFirstRound(Model model) {
        TableViewDTO tableView = tablesService.setupTable(FIRST_ROUND, model);

        model.addAttribute("roundNumber", FIRST_ROUND);
        model.addAttribute("matchColumns", tableView.getMatchesColumns());
        model.addAttribute("tableRows", tableView.getTableRows());

        return "tables/stage-table";
    }

    @Override
    public String getSecondRound(Model model) {
        TableViewDTO tableView = tablesService.setupTable(SECOND_ROUND, model);

        model.addAttribute("roundNumber", SECOND_ROUND);
        model.addAttribute("matchColumns", tableView.getMatchesColumns());
        model.addAttribute("tableRows", tableView.getTableRows());

        return "tables/stage-table";
    }

    @Override
    public String getThirdRound(Model model) {
        TableViewDTO tableView = tablesService.setupTable(THIRD_ROUND, model);

        model.addAttribute("roundNumber", THIRD_ROUND);
        model.addAttribute("matchColumns", tableView.getMatchesColumns());
        model.addAttribute("tableRows", tableView.getTableRows());

        return "tables/stage-table";
    }

    @Override
    public String getKnockoutStage(Model model) {
        TableViewDTO tableView = tablesService.setupTable(KNOCKOUT_STAGE, model);

        model.addAttribute("roundNumber", KNOCKOUT_STAGE);
        model.addAttribute("matchColumns", tableView.getMatchesColumns());
        model.addAttribute("tableRows", tableView.getTableRows());

        return "tables/stage-table";
    }

    @Override
    public String getStagesCombinedScore(Model model) {
        TableViewDTO tableView = tablesService.setupTable(ALL_STAGES_COMBINED, model);

        model.addAttribute("roundNumber", ALL_STAGES_COMBINED);
        model.addAttribute("matchColumns", tableView.getMatchesColumns());
        model.addAttribute("tableRows", tableView.getTableRows());

        return "tables/stage-total";
    }

    @Override
    public String getTournamentLeaderboard(Model model) {
        List<LeaderboardTableRowDTO> tableRows = tablesService.setupLeaderboardTable();

        model.addAttribute("tableRows", tableRows);

        return "tables/tables";
    }
}
