package org.chous.bets.service;

import org.chous.bets.exception.DataNotFoundException;
import org.chous.bets.model.dto.TeamDTO;
import org.chous.bets.model.entity.Team;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TeamServiceAPI {

    List<TeamDTO> findAll();

    TeamDTO findById(int id);

    void create(TeamDTO teamDTO);

    void update(int id, TeamDTO teamDTO);

    void delete(int id);

    /**
     * Загружает команды по идентификаторам и возвращает Map<teamId, Team>.
     *
     * @param teamIds уникальные ID команд
     * @return отображение ID → Team
     * @throws DataNotFoundException если хотя бы одна команда отсутствует
     */
    Map<Integer, Team> getTeamsByIds(Set<Integer> teamIds);
}
