package org.chous.bets.service.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.mapper.StageMapper;
import org.chous.bets.model.dto.StageDTO;
import org.chous.bets.model.entity.Stage;
import org.chous.bets.repository.StageRepository;
import org.chous.bets.service.StageServiceAPI;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StageServiceImpl implements StageServiceAPI {

    private final StageRepository stageRepository;
    private final StageMapper stageMapper;

    @Override
    public List<StageDTO> findAll() {
        return stageRepository.findAll()
                .stream()
                .map(stageMapper::toDto)
                .toList();
    }

    @Override
    public StageDTO findById(Integer id) {
        return stageMapper.toDto(stageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stage not found with id: " + id)));
    }

    @Override
    @Transactional
    public void save(StageDTO stageDTO) {
        stageRepository.save(stageMapper.toEntity(stageDTO));
    }

    @Override
    @Transactional
    public void update(Integer id, StageDTO stageDTO) {
        Stage stage = stageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stage not found with id: " + id));
        stage.setName(stageDTO.getName());
        stage.setKnockoutStage(stageDTO.isKnockoutStage());
        stageRepository.save(stage);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        stageRepository.deleteById(id);
    }
}
