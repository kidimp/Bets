package org.chous.bets.service;

import org.chous.bets.model.dto.RoundDTO;

import java.util.List;

public interface RoundServiceAPI {

    List<RoundDTO> findAll();

    RoundDTO findById(Integer id);

    void save(RoundDTO roundDTO);

    void update(Integer id, RoundDTO roundDTO);

    void delete(Integer id);
}
