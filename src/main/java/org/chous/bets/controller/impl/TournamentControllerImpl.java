package org.chous.bets.controller.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.controller.TournamentControllerAPI;
import org.chous.bets.model.dto.TournamentDTO;
import org.chous.bets.service.TournamentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class TournamentControllerImpl implements TournamentControllerAPI {

    private final TournamentService tournamentService;

    @Override
    public String getAllTournaments(Model model) {
        List<TournamentDTO> tournaments = tournamentService.findAllSortedByDateDesc();
        model.addAttribute("tournaments", tournaments);
        return "tournaments/all";
    }

    @Override
    public String createTournamentForm(TournamentDTO tournamentDTO, Model model) {
        return "";
    }

    @Override
    public String createTournament(TournamentDTO tournamentDTO, BindingResult result, Model model) {
        return "";
    }

    @Override
    public String editTournamentForm(Integer id, Model model) {
        return "";
    }

    @Override
    public String updateTournament(Integer id, TournamentDTO tournamentDTO, BindingResult result, Model model) {
        return "";
    }

    @Override
    public String deleteTournament(Integer id) {
        return "";
    }
}
