package org.chous.bets.dao;

import org.chous.bets.models.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

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
        jdbcTemplate.update("INSERT INTO stages (name, isKnockoutStage) VALUES (?, ?)", stage.getName(), stage.isKnockoutStage());
    }


    public void update(int id, Stage updatedStage) {
        jdbcTemplate.update("UPDATE stages SET name=?, isKnockoutStage=? WHERE id=?",
                updatedStage.getName(), updatedStage.isKnockoutStage(), id);
    }


    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM stages WHERE id=?", id);
    }
}