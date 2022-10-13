package org.chous.bets.dao;

import org.chous.bets.models.Bet;
import org.chous.bets.models.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

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


    public Bet show(int id) {
        return jdbcTemplate.query("SELECT * FROM bets WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Bet.class))
                .stream().findAny().orElse(null);
    }


    public void save(Bet bet) {
        jdbcTemplate.update("INSERT INTO bets (userId, matchId, scoreHomeTeam, scoreAwayTeam," +
                        "isExtraTime, isPenalty) VALUES (?, ?, ?, ?, ?, ?)",
                bet.getUserId(), bet.getMatchId(), bet.getScoreHomeTeam(), bet.getScoreAwayTeam(),
                bet.isExtraTime(), bet.isPenalty());
    }

}
