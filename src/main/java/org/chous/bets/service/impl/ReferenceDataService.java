package org.chous.bets.service.impl;

import lombok.AllArgsConstructor;
import org.chous.bets.model.dto.RoundDTO;
import org.chous.bets.model.dto.StageDTO;
import org.chous.bets.model.dto.TeamDTO;
import org.chous.bets.service.RoundService;
import org.chous.bets.service.StageService;
import org.chous.bets.service.TeamService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReferenceDataService {

    private final TeamService teamService;
    private final StageService stageService;
    private final RoundService roundService;

    @Cacheable(cacheNames = "teams-stages-rounds")
    public ReferenceData loadReferenceData() {
        return new ReferenceData(
                teamService.findAll(),
                stageService.findAll(),
                roundService.findAll()
        );
    }

    public record ReferenceData(List<TeamDTO> teams, List<StageDTO> stages, List<RoundDTO> rounds) {
    }
}
