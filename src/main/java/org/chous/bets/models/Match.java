package org.chous.bets.models;

import org.chous.bets.dao.TeamDAO;
import org.springframework.format.annotation.DateTimeFormat;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.validation.constraints.Min;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Match {
    private int id;
    @DateTimeFormat(pattern = "dd.MM.yyy HH:mm")
    private Date dateAndTime;
    private int stageId;
    private int homeTeamId;
    private int awayTeamId;
    private boolean isFinished;
    @Min(value = 0, message = "Value cannot be negative")
    private int scoreHomeTeam;
    @Min(value = 0, message = "Value cannot be negative")
    private int scoreAwayTeam;

    public Match() {
    }

    private TeamDAO teamDAO;

    public Match(TeamDAO teamDAO) {
        this.teamDAO = teamDAO;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Date getDateAndTime() {
        return dateAndTime;
    }

    public String getDateInStr() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return formatter.format(dateAndTime);
    }

    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public void setDateAndTime(String dateInString) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            this.dateAndTime = formatter.parse(dateInString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }


    public int getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(int homeTeamId) {
        this.homeTeamId = homeTeamId;
    }


    public int getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(int awayTeamId) {
        this.awayTeamId = awayTeamId;
    }


//    public String getTeamName(int teamId) {
//        List<Team> teams = teamDAO.teams();
//        for (Team team : teams) {
//            if (team.getId() == teamId) {
//                return team.getName();
//            }
//        }
//        return "Team name not found!";
//    }


    public boolean isFinished() {
        return isFinished;
    }

    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }


    public int getScoreHomeTeam() {
        return scoreHomeTeam;
    }

    public void setScoreHomeTeam(int scoreHomeTeam) {
        this.scoreHomeTeam = scoreHomeTeam;
    }


    public int getScoreAwayTeam() {
        return scoreAwayTeam;
    }

    public void setScoreAwayTeam(int scoreAwayTeam) {
        this.scoreAwayTeam = scoreAwayTeam;
    }


    public static final Comparator<Match> COMPARE_BY_DATE = new Comparator<Match>() {
        @Override
        public int compare(Match lhs, Match rhs) {
            return Long.compare(lhs.getDateAndTime().getTime(), rhs.getDateAndTime().getTime());
        }
    };
}