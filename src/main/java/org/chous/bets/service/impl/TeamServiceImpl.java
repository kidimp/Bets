package org.chous.bets.service.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.exception.DataNotFoundException;
import org.chous.bets.mapper.TeamMapper;
import org.chous.bets.model.dto.TeamDTO;
import org.chous.bets.model.entity.Team;
import org.chous.bets.repository.BetRepository;
import org.chous.bets.repository.ExtraPointsRepository;
import org.chous.bets.repository.MatchRepository;
import org.chous.bets.repository.TeamRepository;
import org.chous.bets.repository.WinningTeamRepository;
import org.chous.bets.service.TeamService;
import org.springframework.cache.annotation.CacheEvict;
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
//todo разобраться, когда над классом, а когда над методом
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final BetRepository betRepository;
    private final ExtraPointsRepository extraPointsRepository;
    private final WinningTeamRepository winningTeamRepository;
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
    @CacheEvict(cacheNames = "teams-stages-rounds", allEntries = true)
    public void create(TeamDTO teamDTO) {
        Team team = teamMapper.toEntity(teamDTO);
        teamRepository.save(team);
    }

    @Override
    @CacheEvict(cacheNames = "teams-stages-rounds", allEntries = true)
    public void update(int id, TeamDTO teamDTO) {
        Team existing = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + id));
        Team updated = teamMapper.toEntity(teamDTO);
        updated.setId(existing.getId());
        teamRepository.save(updated);
    }

    @Override
    @CacheEvict(cacheNames = "teams-stages-rounds", allEntries = true)
    public void delete(int id) {
        List<Integer> matchIds = matchRepository.getMatchIdsByTeamId(id);
        if (!matchIds.isEmpty()) {
            betRepository.deleteAllByMatchIds(matchIds);
            matchRepository.deleteAllByIds(matchIds);
        }
        extraPointsRepository.deleteAllByTeamId(id);
        winningTeamRepository.deleteAllByTeamId(id);
        teamRepository.deleteById(id);
        // todo переделать на каскадное удаление
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
