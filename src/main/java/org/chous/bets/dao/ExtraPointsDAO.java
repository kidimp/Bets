package org.chous.bets.dao;

import org.chous.bets.models.ExtraPoints;
import org.chous.bets.models.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExtraPointsDAO {

    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public ExtraPointsDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Team> teams() {
        return jdbcTemplate.query("SELECT * FROM extra_points_by_user", new BeanPropertyRowMapper<>(Team.class));
    }


    public ExtraPoints showByUser(int userId) {
        return jdbcTemplate.query("SELECT * FROM extra_points_by_user WHERE userId=?;", new Object[]{userId},
                new BeanPropertyRowMapper<>(ExtraPoints.class)).stream().findAny().orElse(null);
    }


    public Integer showWinningTeamIdByUser(int userId) {
        try {
            return jdbcTemplate.queryForObject("SELECT teamId FROM extra_points_by_user WHERE userId=?;",
                    new Object[]{userId}, Integer.class);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }


    public void saveExtraPointsByUser(int userId, ExtraPoints extraPoints) {
        jdbcTemplate.update("INSERT INTO extra_points_by_user (userId, teamId, numberOfHitsOnTheCorrectScore, numberOfHitsOnTheMatchResult, extraPoints) VALUES (?, ?, ?, ?, ?)",
                userId, extraPoints.getWinningTeamId(), extraPoints.getNumberOfHitsOnTheCorrectScore(), extraPoints.getNumberOfHitsOnTheMatchResult(), extraPoints.getExtraPoints());
    }


    public void updateWinningTeamByUser(int userId, int teamId) {
        jdbcTemplate.update("UPDATE extra_points_by_user SET teamId=? WHERE userId=?", teamId, userId);
    }

    public void updateExtraPointsByUser(int userId, double extraPoints) {
        jdbcTemplate.update("UPDATE extra_points_by_user SET extraPoints=? WHERE userId=?", extraPoints, userId);
    }


    public void updateNumberOfHitsOnTheCorrectScoreAndNumberOfHitsOnTheMatchResult(int userId, int numberOfHitsOnTheCorrectScore, int numberOfHitsOnTheMatchResult) {
        jdbcTemplate.update("UPDATE extra_points_by_user SET numberOfHitsOnTheCorrectScore=?, numberOfHitsOnTheMatchResult=? WHERE userId=?",
                numberOfHitsOnTheCorrectScore, numberOfHitsOnTheMatchResult, userId);
    }


    public void saveWinningTeam(ExtraPoints extraPoints) {
        jdbcTemplate.update("INSERT INTO winning_team (id, dateAndTime, winningTeamId, secondPlaceTeamId) VALUES (?, ?, ?, ?)",
                1, extraPoints.getDateAndTime(), extraPoints.getWinningTeamId(), extraPoints.getSecondPlaceTeamId());
    }


    public void updateWinningTeam(ExtraPoints extraPoints) {
        jdbcTemplate.update("UPDATE winning_team SET dateAndTime=?, winningTeamId=?, secondPlaceTeamId=? WHERE id=1",
                extraPoints.getDateAndTime(), extraPoints.getWinningTeamId(), extraPoints.getSecondPlaceTeamId());
    }


    public ExtraPoints show() {
        return jdbcTemplate.query("SELECT * FROM winning_team WHERE id=1;", new Object[]{},
                new BeanPropertyRowMapper<>(ExtraPoints.class)).stream().findAny().orElse(null);
    }


}