package org.chous.bets.dao;

import org.chous.bets.models.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class StageDAO {

    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public StageDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Stage> stages() {
        return jdbcTemplate.query("SELECT * FROM stages", new BeanPropertyRowMapper<>(Stage.class));
    }


    public Stage show(int id) {
        return jdbcTemplate.query("SELECT * FROM stages WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Stage.class))
                .stream().findAny().orElse(null);
    }


    public void save(Stage stage) {
        jdbcTemplate.update("INSERT INTO stages (name) VALUES (?)", stage.getName());
    }


    public void update(int id, Stage updatedStage) {
        jdbcTemplate.update("UPDATE stages SET name=? WHERE id=?",
                updatedStage.getName(), id);
    }


    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM stages WHERE id=?", id);
    }
}