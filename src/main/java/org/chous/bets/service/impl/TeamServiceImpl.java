package org.chous.bets.service.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.exception.DataNotFoundException;
import org.chous.bets.mapper.TeamMapper;
import org.chous.bets.model.dto.TeamDTO;
import org.chous.bets.model.entity.Team;
import org.chous.bets.repository.TeamRepository;
import org.chous.bets.service.TeamServiceAPI;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamServiceImpl implements TeamServiceAPI {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TeamDTO> findAll() {
        return teamRepository.findAll()
                .stream()
                .map(teamMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TeamDTO findById(int id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + id));
        return teamMapper.toDto(team);
    }

    @Override
    public void create(TeamDTO teamDTO) {
        Team team = teamMapper.toEntity(teamDTO);
        teamRepository.save(team);
    }

    @Override
    public void update(int id, TeamDTO teamDTO) {
        Team existing = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + id));
        Team updated = teamMapper.toEntity(teamDTO);
        updated.setId(existing.getId());
        teamRepository.save(updated);
    }

    @Override
    public void delete(int id) {
        teamRepository.deleteById(id);
    }

    @Override
    public Map<Integer, Team> getTeamsByIds(Set<Integer> teamIds) {
        List<Team> teams = teamRepository.findAllById(teamIds);

        if (teams.size() != teamIds.size()) {
            Set<Integer> foundIds = teams.stream()
                    .map(Team::getId)
                    .collect(Collectors.toSet());
            Set<Integer> missing = new HashSet<>(teamIds);
            missing.removeAll(foundIds);
            throw new DataNotFoundException("Найдены не все команды: " + missing);
        }

        return teams.stream()
                .collect(Collectors.toMap(Team::getId, Function.identity()));
    }
}
