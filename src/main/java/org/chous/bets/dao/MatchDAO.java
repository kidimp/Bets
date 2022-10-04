package org.chous.bets.dao;

import org.chous.bets.models.Match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MatchDAO {

    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public MatchDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Match> matches() {
        return jdbcTemplate.query("SELECT * FROM matches", new BeanPropertyRowMapper<>(Match.class));
    }


    public Match show(int id) {
        return jdbcTemplate.query("SELECT * FROM matches WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Match.class))
                .stream().findAny().orElse(null);
    }


    public void save(Match match) {
        jdbcTemplate.update("INSERT INTO matches (dateAndTime, stageId, homeTeamId, awayTeamId," +
                        "isInProgress, isFinished, scoreHomeTeam, scoreAwayTeam) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                match.getDateAndTime(), match.getStageId(), match.getHomeTeamId(), match.getAwayTeamId(),
                match.isInProgress(), match.isFinished(), match.getScoreHomeTeam(), match.getScoreAwayTeam());
    }


    public void update(int id, Match updatedMatch) {
        jdbcTemplate.update("UPDATE matches SET dateAndTime=?, stageId=?, homeTeamId=?, awayTeamId=?," +
                        "isInProgress=?, isFinished=?, scoreHomeTeam=?, scoreAwayTeam=? WHERE id=?",
                updatedMatch.getDateAndTime(), updatedMatch.getStageId(), updatedMatch.getHomeTeamId(), updatedMatch.getAwayTeamId(),
                updatedMatch.isInProgress(), updatedMatch.isFinished(), updatedMatch.getScoreHomeTeam(), updatedMatch.getScoreAwayTeam(), id);
    }


    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM matches WHERE id=?", id);
    }
}
