package org.chous.bets.dao;

import org.chous.bets.model.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoundDAO {
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public RoundDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Round> rounds() {
        return jdbcTemplate.query("SELECT * FROM rounds", new BeanPropertyRowMapper<>(Round.class));
    }


    public Round show(int id) {
        return jdbcTemplate.query("SELECT * FROM rounds WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Round.class))
                .stream().findAny().orElse(null);
    }


    public void save(Round round) {
        jdbcTemplate.update("INSERT INTO rounds (name, round) VALUES (?, ?)", round.getName(), round.getRound());
    }


    public void update(int id, Round updatedRound) {
        jdbcTemplate.update("UPDATE rounds SET name=?, round=? WHERE id=?",
                updatedRound.getName(), updatedRound.getRound(), id);
    }


    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM rounds WHERE id=?", id);
    }

}
