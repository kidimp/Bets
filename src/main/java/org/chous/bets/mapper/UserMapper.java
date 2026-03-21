package org.chous.bets.mapper;

import org.chous.bets.model.dto.UserDTO;
import org.chous.bets.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(User user);

    User toEntity(UserDTO dto);
}
