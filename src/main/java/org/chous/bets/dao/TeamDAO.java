//package org.chous.bets.dao;
//
//import org.chous.bets.model.Team;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class TeamDAO {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public TeamDAO(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//
//    public List<Team> teams() {
//        return jdbcTemplate.query("SELECT * FROM teams", new BeanPropertyRowMapper<>(Team.class));
//    }
//
//
//    public Team show(int id) {
//        return jdbcTemplate.query("SELECT * FROM teams WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Team.class))
//                .stream().findAny().orElse(null);
//    }
//
//
//    public void save(Team team) {
//        jdbcTemplate.update("INSERT INTO teams (name, isoName, pot) VALUES (?, ?, ?)", team.getName(), team.getIsoName(), team.getPot());
//    }
//
//
//    public void update(int id, Team updatedTeam) {
//        jdbcTemplate.update("UPDATE teams SET name=?, isoName=?, pot=? WHERE id=?",
//                updatedTeam.getName(), updatedTeam.getIsoName(), updatedTeam.getPot(), id);
//    }
//
//
//    public void delete(int id) {
//        jdbcTemplate.update("DELETE FROM teams WHERE id=?", id);
//    }
//
//}
