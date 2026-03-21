package org.chous.bets.mapper;

import org.chous.bets.model.dto.MatchDTO;
import org.chous.bets.model.entity.Match;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MatchMapper {

    MatchDTO toDto(Match match);

    Match toEntity(MatchDTO dto);
}
