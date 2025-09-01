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

    // Обязательно: чтобы избежать ошибок при создании пустого User/Match с нужным ID
//    default User mapUser(Long id) {
//        if (id == null) return null;
//        User user = new User();
//        user.setId(id.intValue());
//        return user;
//    }
//
//    default Match mapMatch(Long id) {
//        if (id == null) return null;
//        Match match = new Match();
//        match.setId(id.intValue());
//        return match;
//    }
//
//    default Long mapUser(User user) {
//        if (user == null) return null;
//        return user.getId() != null ? user.getId().longValue() : null;
//    }
//
//    default Long mapMatch(Match match) {
//        if (match == null) return null;
//        return match.getId() != null ? match.getId().longValue() : null;
//    }
}
