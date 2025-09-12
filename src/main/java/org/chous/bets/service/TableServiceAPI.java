package org.chous.bets.service;

import org.chous.bets.model.dto.LeaderboardTableRowDTO;
import org.chous.bets.model.dto.TableViewDTO;
import org.springframework.ui.Model;

import java.util.List;

public interface TableServiceAPI {

    TableViewDTO setupTable(int roundNumber, Model model);

    List<LeaderboardTableRowDTO> setupLeaderboardTable();
}
