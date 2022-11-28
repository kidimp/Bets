package org.chous.bets.dao;

import org.chous.bets.models.Team;
import org.chous.bets.models.WinningTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WinningTeamDAO {

    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public WinningTeamDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Team> teams() {
        return jdbcTemplate.query("SELECT * FROM winning_team_by_user", new BeanPropertyRowMapper<>(Team.class));
    }


    public Team show(int userId) {
        return jdbcTemplate.query("SELECT * FROM winning_team_by_user WHERE userId=?;", new Object[]{userId},
                new BeanPropertyRowMapper<>(Team.class)).stream().findAny().orElse(null);
    }


    public Integer showWinningTeamId(int userId) {
        try {
            return  jdbcTemplate.queryForObject("SELECT teamId FROM winning_team_by_user WHERE userId=?;",
                    new Object[]{userId}, Integer.class);
        }
        catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }


    public void save(int userId, int teamId) {
        jdbcTemplate.update("INSERT INTO winning_team_by_user (userId, teamId) VALUES (?, ?)", userId, teamId);
    }


    public void update(int userId, int teamId) {
        jdbcTemplate.update("UPDATE winning_team_by_user SET teamId=? WHERE userId=?", teamId, userId);
    }



    public void saveWinningTeam(WinningTeam winningTeam) {
        jdbcTemplate.update("INSERT INTO winning_team (id, dateAndTime, winningTeamId) VALUES (?, ?, ?)",
                1, winningTeam.getDateAndTime(), winningTeam.getWinningTeamId());
    }


    public void updateWinningTeam(WinningTeam winningTeam) {
        jdbcTemplate.update("UPDATE winning_team SET dateAndTime=?, winningTeamId=? WHERE id=1",
                winningTeam.getDateAndTime(), winningTeam.getWinningTeamId());
    }


    public WinningTeam showWinningTeam() {
        return jdbcTemplate.query("SELECT * FROM winning_team WHERE id=1;", new Object[]{},
                new BeanPropertyRowMapper<>(WinningTeam.class)).stream().findAny().orElse(null);
    }



}