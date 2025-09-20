package org.chous.bets.mapper;

import org.chous.bets.model.dto.TournamentDTO;
import org.chous.bets.model.entity.Tournament;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TournamentMapper {

    TournamentDTO toDto(Tournament tournament);

    Tournament toEntity(TournamentDTO dto);
}
