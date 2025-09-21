package org.chous.bets.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.chous.bets.model.entity.Bet;
import org.chous.bets.model.entity.ExtraPoints;
import org.chous.bets.model.entity.Match;
import org.chous.bets.model.entity.Team;
import org.chous.bets.model.entity.User;
import org.chous.bets.model.entity.WinningTeam;
import org.chous.bets.repository.BetRepository;
import org.chous.bets.repository.ExtraPointsRepository;
import org.chous.bets.repository.MatchRepository;
import org.chous.bets.repository.TeamRepository;
import org.chous.bets.repository.UserRepository;
import org.chous.bets.repository.WinningTeamRepository;
import org.chous.bets.service.PointsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.chous.bets.util.Constants.TOURNAMENT_WINNING_TEAM_ID;

@Service
@RequiredArgsConstructor
public class PointsServiceImpl implements PointsService {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final BetRepository betRepository;
    private final ExtraPointsRepository extraPointsRepository;
    private final WinningTeamRepository winningTeamRepository;

    @Override
    @Transactional
    public void recalculatePoints() {
        List<Match> matches = matchRepository.findAll();
        List<Team> teams = teamRepository.findAll();
        List<User> users = userRepository.findAllActiveUsers();
        List<Bet> bets = betRepository.findAll();

        Map<Integer, Integer> correctScoreMap = initUserMap(users);
        Map<Integer, Integer> matchResultMap = initUserMap(users);

        for (Bet bet : bets) {
            Match match = findMatchById(bet.getMatch().getId(), matches);
            if (match != null && match.isFinished()) {
                Team homeTeam = findTeamById(match.getHomeTeamId(), teams);
                Team awayTeam = findTeamById(match.getAwayTeamId(), teams);
                PointsCalculator calculator = new PointsCalculator(bet, match, homeTeam, awayTeam);

                double points = PointsCalculator.round(calculator.getPointsForMatch(), 2);
                bet.setPoints(points);
                betRepository.updatePoints(bet.getId(), points);
int i = bet.getUser().getId();
                updateHitStats(bet.getUser().getId(), calculator, correctScoreMap, matchResultMap);
            }
        }

        updateExtraPointsStats(correctScoreMap, matchResultMap);
        updateExtraPointsValues(users);
    }

    private Map<Integer, Integer> initUserMap(List<User> users) {
        return users.stream().collect(Collectors.toMap(User::getId, u -> 0));
    }

    private void updateHitStats(int userId, PointsCalculator calc,
                                Map<Integer, Integer> correctMap, Map<Integer, Integer> resultMap) {
        if (calc.isHitOnTheCorrectScore()) {
            correctMap.compute(userId, (k, v) -> v + 1);
        }
        if (calc.isHitOnTheMatchResult()) {
            resultMap.compute(userId, (k, v) -> v + 1);
        }
    }

    private void updateExtraPointsStats(Map<Integer, Integer> correctMap, Map<Integer, Integer> resultMap) {
        for (Map.Entry<Integer, Integer> entry : correctMap.entrySet()) {
            int userId = entry.getKey();
            int correct = entry.getValue();
            int result = resultMap.getOrDefault(userId, 0);

            ExtraPoints points = extraPointsRepository.findByUserId(userId).orElseGet(ExtraPoints::new);
            points.setUserId(userId);
            points.setNumberOfHitsOnCorrectScore(correct);
            points.setNumberOfHitsOnMatchResult(result);
            if (points.getWinningTeamId() == null) {
                points.setWinningTeamId(0);
            }
            extraPointsRepository.save(points);
        }
    }

    private void updateExtraPointsValues(List<User> users) {
        int winningTeamId = winningTeamRepository
                .getTournamentWinningTeam(TOURNAMENT_WINNING_TEAM_ID)
                .map(WinningTeam::getWinningTeamId)
                .orElse(0);
        int secondTeamId = winningTeamRepository
                .getTournamentWinningTeam(TOURNAMENT_WINNING_TEAM_ID)
                .map(WinningTeam::getSecondPlaceTeamId)
                .orElse(0);

        for (User user : users) {
            int userId = user.getId();
            int predictedWinner = extraPointsRepository.findWinningTeamIdByUserId(userId).orElse(0);
            ExtraPoints stats = extraPointsRepository.findByUserId(userId).orElse(new ExtraPoints());

            int correctScoreHits = stats.getNumberOfHitsOnCorrectScore() / 5;
            int matchResultHits = stats.getNumberOfHitsOnMatchResult() / 5;

            PointsCalculator calc = new PointsCalculator(winningTeamId, secondTeamId, predictedWinner);
            double totalExtraPoints = calc.getExtraPointsForWinningTeam()
                    + (correctScoreHits * 5.0)
                    + (matchResultHits * 3.0);

            extraPointsRepository.updateExtraPointsByUserId(userId, totalExtraPoints);
        }
    }

    private Match findMatchById(int id, List<Match> matches) {
        return matches.stream().filter(m -> m.getId() == id).findFirst().orElse(null);
    }

    private Team findTeamById(int id, List<Team> teams) {
        return teams.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }
}
