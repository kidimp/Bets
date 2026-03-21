package org.chous.bets.mapper;

import org.chous.bets.model.dto.StageDTO;
import org.chous.bets.model.entity.Stage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StageMapper {

    StageDTO toDto(Stage stage);

    Stage toEntity(StageDTO dto);
}
