package org.chous.bets.mapper;

import org.chous.bets.model.dto.RoundDTO;
import org.chous.bets.model.entity.Round;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoundMapper {

    RoundDTO toDto(Round round);

    Round toEntity(RoundDTO dto);
}