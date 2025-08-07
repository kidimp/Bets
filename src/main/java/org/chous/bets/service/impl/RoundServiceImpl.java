package org.chous.bets.service.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.mapper.RoundMapper;
import org.chous.bets.model.dto.RoundDTO;
import org.chous.bets.model.entity.Match;
import org.chous.bets.model.entity.Round;
import org.chous.bets.repository.MatchRepository;
import org.chous.bets.repository.RoundRepository;
import org.chous.bets.service.MatchServiceAPI;
import org.chous.bets.service.RoundServiceAPI;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoundServiceImpl implements RoundServiceAPI {

    private final RoundRepository roundRepository;
    private final RoundMapper roundMapper;
    private final MatchRepository matchRepository;
    private final MatchServiceAPI matchService;

    @Override
    public List<RoundDTO> findAll() {
        return roundRepository.findAll().stream()
                .map(roundMapper::toDto)
                .toList();
    }

    @Override
    public RoundDTO findById(Integer id) {
        return roundRepository.findById(id)
                .map(roundMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Раунд не найден, id = " + id));
    }

    @Override
    @Transactional
    public void save(RoundDTO roundDTO) {
        roundRepository.save(roundMapper.toEntity(roundDTO));
    }

    @Override
    @Transactional
    public void update(Integer id, RoundDTO roundDTO) {
        Round existing = roundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Раунд не найден, id = " + id));

        existing.setName(roundDTO.getName());
        existing.setRound(roundDTO.getRound());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        List<Match> matches = matchRepository.findAllByRound(id);
        for (Match match : matches) {
            matchService.delete(match.getId());
        }
        roundRepository.deleteById(id);
    }
}
