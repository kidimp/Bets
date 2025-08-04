package org.chous.bets.service;

import org.chous.bets.model.dto.StageDTO;

import java.util.List;

public interface StageServiceAPI {

    List<StageDTO> findAll();

    StageDTO findById(Integer id);

    void save(StageDTO stageDTO);

    void update(Integer id, StageDTO stageDTO);

    void delete(Integer id);
}
