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
        jdbcTemplate.update("INSERT INTO matches(dateAndTime, stageId, homeTeamId, awayTeamId, " +
                        "isFinished, scoreHomeTeam, scoreAwayTeam, isExtraTime, isPenalty) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                match.getDateAndTime(), match.getStageId(), match.getHomeTeamId(), match.getAwayTeamId(),
                match.isFinished(), match.getScoreHomeTeam(), match.getScoreAwayTeam(), match.isExtraTime(), match.isPenalty());
    }


    public void update(int id, Match updatedMatch) {
        jdbcTemplate.update("UPDATE matches SET dateAndTime=?, stageId=?, homeTeamId=?, awayTeamId=?, " +
                        "isFinished=?, scoreHomeTeam=?, scoreAwayTeam=?, isExtraTime=?, isPenalty=? WHERE id=?",
                updatedMatch.getDateAndTime(), updatedMatch.getStageId(),
                updatedMatch.getHomeTeamId(), updatedMatch.getAwayTeamId(),
                updatedMatch.isFinished(),
                updatedMatch.getScoreHomeTeam(), updatedMatch.getScoreAwayTeam(),
                updatedMatch.isExtraTime(), updatedMatch.isPenalty(), id);
    }


    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM matches WHERE id=?", id);
    }


    public String getHomeTeamName(int homeTeamId) {
        return jdbcTemplate.query("SELECT matches.homeTeamId, teams.name FROM matches " +
                                "INNER JOIN teams ON matches.homeTeamId = teams.id WHERE homeTeamId = ?;",
                        new Object[]{homeTeamId}, new BeanPropertyRowMapper<>(String.class))
                .stream().findAny().orElse(null);
    }
}
