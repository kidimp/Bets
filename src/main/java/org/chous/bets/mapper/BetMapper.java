package org.chous.bets.mapper;

import org.chous.bets.model.dto.BetDTO;
import org.chous.bets.model.entity.Bet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BetMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "match.id", source = "matchId")
    @Mapping(target = "scoreHomeTeam", source = "scoreHomeTeam")
    @Mapping(target = "scoreAwayTeam", source = "scoreAwayTeam")
    Bet toEntity(BetDTO dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "matchId", source = "match.id")
    @Mapping(target = "scoreHomeTeam", source = "scoreHomeTeam")
    @Mapping(target = "scoreAwayTeam", source = "scoreAwayTeam")
    BetDTO toDto(Bet entity);
}
