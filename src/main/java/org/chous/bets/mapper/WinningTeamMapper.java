package org.chous.bets.mapper;

import org.chous.bets.model.dto.WinningTeamTournamentDTO;
import org.chous.bets.model.entity.WinningTeam;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WinningTeamMapper {

    WinningTeamTournamentDTO toDto(WinningTeam entity);

    WinningTeam toEntity(WinningTeamTournamentDTO dto);
}
