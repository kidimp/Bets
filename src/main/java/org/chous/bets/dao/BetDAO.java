package org.chous.bets.dao;

import org.chous.bets.models.Bet;
import org.chous.bets.models.Match;
import org.chous.bets.models.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class BetDAO {

    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public BetDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Bet> bets() {
        return jdbcTemplate.query("SELECT * FROM bets", new BeanPropertyRowMapper<>(Bet.class));
    }


    public Bet show(int userId, int matchId) {
        return jdbcTemplate.query("SELECT * FROM bets WHERE userId=? AND matchId=?;", new Object[]{userId, matchId},
                        new BeanPropertyRowMapper<>(Bet.class))
                .stream().findAny().orElse(null);
    }


    public void save(Bet bet) {
        jdbcTemplate.update("INSERT INTO bets (userId, matchId, scoreHomeTeam, scoreAwayTeam," +
                        "isExtraTime, isPenalty, points) VALUES (?, ?, ?, ?, ?, ?, ?)",
                bet.getUserId(), bet.getMatchId(), bet.getScoreHomeTeam(), bet.getScoreAwayTeam(),
                bet.isExtraTime(), bet.isPenalty(), bet.getPoints());
    }


    public void update(int id, Bet updatedBet) {
        jdbcTemplate.update("UPDATE bets SET scoreHomeTeam=?, scoreAwayTeam=?, isExtraTime=?, isPenalty=? WHERE id=?",
                updatedBet.getScoreHomeTeam(), updatedBet.getScoreAwayTeam(), updatedBet.isExtraTime(), updatedBet.isPenalty(), id);
    }


    public void updatePoints(int id, Bet updatedBet) {
        jdbcTemplate.update("UPDATE bets SET points=? WHERE id=?",
                updatedBet.getPoints(), id);
    }

}
