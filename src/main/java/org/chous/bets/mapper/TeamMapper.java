package org.chous.bets.mapper;

import org.chous.bets.model.dto.TeamDTO;
import org.chous.bets.model.entity.Team;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamDTO toDto(Team team);

    Team toEntity(TeamDTO dto);
}
