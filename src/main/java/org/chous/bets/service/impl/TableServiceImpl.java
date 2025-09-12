package org.chous.bets.service.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.exception.DataNotFoundException;
import org.chous.bets.model.dto.BetViewDTO;
import org.chous.bets.model.dto.LeaderboardTableRowDTO;
import org.chous.bets.model.dto.MatchColumnDTO;
import org.chous.bets.model.dto.TableRowDTO;
import org.chous.bets.model.dto.TableViewDTO;
import org.chous.bets.model.entity.Bet;
import org.chous.bets.model.entity.ExtraPoints;
import org.chous.bets.model.entity.Match;
import org.chous.bets.model.entity.Team;
import org.chous.bets.model.entity.User;
import org.chous.bets.repository.BetRepository;
import org.chous.bets.repository.ExtraPointsRepository;
import org.chous.bets.repository.MatchRepository;
import org.chous.bets.repository.TeamRepository;
import org.chous.bets.repository.UserRepository;
import org.chous.bets.repository.WinningTeamRepository;
import org.chous.bets.service.TableServiceAPI;
import org.chous.bets.service.TeamServiceAPI;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.chous.bets.util.Constants.ALL_STAGES_COMBINED;
import static org.chous.bets.util.Constants.AMOUNT_OF_ROUNDS;
import static org.chous.bets.util.Constants.FIRST_ROUND;
import static org.chous.bets.util.Constants.KNOCKOUT_STAGE;
import static org.chous.bets.util.Constants.SECOND_ROUND;
import static org.chous.bets.util.Constants.THIRD_ROUND;
import static org.chous.bets.util.Constants.TOURNAMENT_WINNING_TEAM_ID;

@Service
@RequiredArgsConstructor
@Transactional
public class TableServiceImpl implements TableServiceAPI {

    private final TeamServiceAPI teamService;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final BetRepository betRepository;
    private final ExtraPointsRepository extraPointsRepository;
    private final TeamRepository teamRepository;
    private final WinningTeamRepository winningTeamRepository;

    private static final String EMPTY_WINNING_TEAM_PREDICTION = "";

    @Override
    public TableViewDTO setupTable(int round, Model model) {
        List<Match> matches = getMatchesForRound(round);
        List<MatchColumnDTO> matchColumns = buildMatchColumns(matches);
        List<TableRowDTO> rows = buildTableRows(matches, round == ALL_STAGES_COMBINED);
        return new TableViewDTO(matchColumns, rows);
    }

    @Override
    public List<LeaderboardTableRowDTO> setupLeaderboardTable() {
        List<User> users = userRepository.findAllActiveUsers();

        Map<Integer, Map<Integer, Double>> roundPoints = Map.of(
                FIRST_ROUND, getUserPointsByRound(FIRST_ROUND),
                SECOND_ROUND, getUserPointsByRound(SECOND_ROUND),
                THIRD_ROUND, getUserPointsByRound(THIRD_ROUND),
                KNOCKOUT_STAGE, getUserPointsByRound(KNOCKOUT_STAGE),
                ALL_STAGES_COMBINED, getUserPointsByRound(ALL_STAGES_COMBINED)
                //todo подумать, можно ли динамически подставлять раунды. Например, сделать RoundEnum
                // т.е. проходиться в цикле по всем доступным раундамroundRanks = {HashMap@14969}  size = 5
        );

        Map<Integer, Map<Integer, Integer>> roundRanks = new HashMap<>();
        for (int round = 0; round <= 4; round++) {
            roundRanks.put(round, computeRankings(roundPoints.get(round)));
        }

        Map<Integer, ExtraPoints> extraPointsByUser = getExtraPointsByUserId(users);
        Map<Integer, Team> teamsById = getTeamsByWinningPrediction(extraPointsByUser.values());

        List<LeaderboardTableRowDTO> leaderboard = new ArrayList<>();

        for (User user : users) {
            int userId = user.getId();

            double totalPointsFirstRound = roundPoints.get(FIRST_ROUND).getOrDefault(userId, 0.0);
            double totalPointsSecondRound = roundPoints.get(SECOND_ROUND).getOrDefault(userId, 0.0);
            double totalPointsThirdRound = roundPoints.get(THIRD_ROUND).getOrDefault(userId, 0.0);
            double totalPointsKnockoutStage = roundPoints.get(KNOCKOUT_STAGE).getOrDefault(userId, 0.0);
            double totalPointsAllStages = roundPoints.get(ALL_STAGES_COMBINED).getOrDefault(userId, 0.0);

            int r1 = roundRanks.get(FIRST_ROUND).getOrDefault(userId, users.size());
            int r2 = roundRanks.get(SECOND_ROUND).getOrDefault(userId, users.size());
            int r3 = roundRanks.get(THIRD_ROUND).getOrDefault(userId, users.size());
            int r4 = roundRanks.get(KNOCKOUT_STAGE).getOrDefault(userId, users.size());
            int r5 = roundRanks.get(ALL_STAGES_COMBINED).getOrDefault(userId, users.size());

            double avgPosition = (r1 + r2 + r3 + r4 + r5) / AMOUNT_OF_ROUNDS;

            ExtraPoints extraPoints = extraPointsByUser.get(userId);

//            double totalPointsAllStagesWithExtraPoints = totalPointsAllStages + extraPoints.getExtraPoints();

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime predictionDeadline = getPredictionDeadline();
            boolean isPredictAvailable = predictionDeadline.isBefore(now);

            String winningTeamName = extraPoints.getWinningTeamId() > 0
                    ? Optional.ofNullable(teamsById.get(extraPoints.getWinningTeamId())).map(Team::getName).orElse("")
                    : "";

            leaderboard.add(new LeaderboardTableRowDTO(
                    user.getUsername(),
                    avgPosition,
                    totalPointsFirstRound,
                    totalPointsSecondRound,
                    totalPointsThirdRound,
                    totalPointsKnockoutStage,
                    totalPointsAllStages,
                    extraPoints.getNumberOfHitsOnCorrectScore(),
                    extraPoints.getNumberOfHitsOnMatchResult(),
                    winningTeamName,
                    isPredictAvailable,
                    extraPoints.getExtraPoints()
            ));
        }

        // Финальная сортировка по position и назначение индексов
        leaderboard.sort(Comparator.comparingDouble(LeaderboardTableRowDTO::getPosition));

        return leaderboard;
    }

    private List<Match> getMatchesForRound(int roundNumber) {
        return roundNumber != 0
                ? matchRepository.findByRoundOrderByDateAndTimeAsc(roundNumber)
                : matchRepository.findAllByOrderByDateAndTimeAsc();
    }

    private List<MatchColumnDTO> buildMatchColumns(List<Match> matches) {
        Set<Integer> teamIds = matches.stream()
                .flatMap(m -> Stream.of(m.getHomeTeamId(), m.getAwayTeamId()))
                .collect(Collectors.toSet());

        Map<Integer, Team> teams = teamService.getTeamsByIds(teamIds);

        return matches.stream()
                .map(match -> MatchColumnDTO.builder()
                        .homeTeamName(teams.get(match.getHomeTeamId()).getName())
                        .homeTeamIsoName(teams.get(match.getHomeTeamId()).getIsoName())
                        .awayTeamName(teams.get(match.getAwayTeamId()).getName())
                        .awayTeamIsoName(teams.get(match.getAwayTeamId()).getIsoName())
                        .scoreHomeTeam(match.getScoreHomeTeam())
                        .scoreAwayTeam(match.getScoreAwayTeam())
                        .finished(match.isFinished())
                        .extraTime(match.isExtraTime())
                        .penalty(match.isPenalty())
                        .build()
                ).toList();
    }

    private List<TableRowDTO> buildTableRows(List<Match> matches, boolean withExtraStats) {
        // Получаем всех активных пользователей
        List<User> users = userRepository.findAllActiveUsers();

        // Создаём Map для быстрого доступа к матчам по их ID
        Map<Integer, Match> matchById = matches.stream()
                .collect(Collectors.toMap(Match::getId, Function.identity()));

        // Загружаем все ставки по матчам (одним запросом)
        List<Bet> allBets = betRepository.findByMatchIdIn(new ArrayList<>(matchById.keySet()));

        LocalDateTime now = LocalDateTime.now();

        // Фильтруем ставки: исключаем те, у которых матч ещё не начался
        List<Bet> filteredBets = allBets.stream()
                .filter(bet -> {
                    Match match = bet.getMatch();
                    return match != null && match.getDateAndTime().isBefore(now);
                })
                .toList();

        // Группируем ставки по пользователю и матчу: userId -> (matchId -> Bet)
        Map<Integer, Map<Integer, Bet>> userBetsMap = buildUserBetsMap(filteredBets);

        // Загружаем все ExtraPoints и все команды (одним запросом).
        // Эти данные нужны только для таблицы, где собраны все матчи и нужны дополнительной колонки (ExtraStats), иначе — возвращаем пусто
        Map<Integer, ExtraPoints> extraPointsByUserId = withExtraStats ? getExtraPointsByUserId(users) : Map.of();

        Map<Integer, Team> teamsById = Map.of();
        if (withExtraStats) {
            LocalDateTime predictionDeadline = getPredictionDeadline();
            if (predictionDeadline.isBefore(now)) {
                teamsById = getTeamsByWinningPrediction(extraPointsByUserId.values());
            }
        }

        List<TableRowDTO> result = new ArrayList<>();

        // Для каждого пользователя формируем строку таблицы
        for (User user : users) {
            // Получаем все ставки пользователя (по нужным матчам)
            Map<Integer, Bet> userBets = userBetsMap.getOrDefault(user.getId(), Collections.emptyMap());
            List<BetViewDTO> betViews = buildBetViews(matches, userBets, now);

            // Считаем суммарные очки за раунд
            double totalPoints = betViews.stream()
                    .filter(Objects::nonNull)
                    .mapToDouble(BetViewDTO::getPoints)
                    .sum();

            double totalPointsWithExtraPoints = totalPoints +
                    Optional.ofNullable(extraPointsByUserId.get(user.getId()))
                            .map(ExtraPoints::getExtraPoints)
                            .orElse(0.0);

            // В зависимости от флага withExtraStats создаём строку с дополнительной статистикой или без неё
            TableRowDTO row = withExtraStats
                    ? buildTableRowWithExtras(user, totalPointsWithExtraPoints, betViews, extraPointsByUserId, teamsById)
                    : TableRowDTO.builder()
                    .username(user.getUsername())
                    .totalPointsForThisRound(totalPoints)
                    .betViews(betViews)
                    .build();

            result.add(row);
        }

        // Сортируем таблицу по убыванию очков
        result.sort(Comparator.comparingDouble(this::getTotalPoints).reversed());
        return result;
    }

    private LocalDateTime getPredictionDeadline() {
        return winningTeamRepository
                .getWinningTeamPredictionDateAndTimeDeadline(TOURNAMENT_WINNING_TEAM_ID)
                .orElseThrow(() -> new DataNotFoundException("Winning Team Prediction Deadline not found"));
    }

    private Map<Integer, Map<Integer, Bet>> buildUserBetsMap(List<Bet> bets) {
        return bets.stream()
                .collect(Collectors.groupingBy(
                        bet -> bet.getUser().getId(),
                        Collectors.toMap(bet -> bet.getMatch().getId(), Function.identity())
                ));
    }

    private Map<Integer, ExtraPoints> getExtraPointsByUserId(List<User> users) {
        List<Integer> userIds = users.stream()
                .map(User::getId)
                .toList();

        return extraPointsRepository.findByUserIdIn(userIds).stream()
                .collect(Collectors.toMap(ExtraPoints::getUserId, Function.identity()));
    }

    private Map<Integer, Team> getTeamsByWinningPrediction(Collection<ExtraPoints> extraPointsList) {
        Set<Integer> winningTeamIds = extraPointsList.stream()
                .map(ExtraPoints::getWinningTeamId)
                .filter(id -> id > 0)
                .collect(Collectors.toSet());

        return teamRepository.findAllById(winningTeamIds).stream()
                .collect(Collectors.toMap(Team::getId, Function.identity()));
    }

    private List<BetViewDTO> buildBetViews(List<Match> matches, Map<Integer, Bet> userBets, LocalDateTime now) {
        return matches.stream()
                .map(match -> {
                    Bet bet = userBets.get(match.getId());
                    if (bet == null) return null;
                    return BetViewDTO.builder()
                            .matchStarted(match.getDateAndTime().isBefore(now))
                            .matchFinished(match.isFinished())
                            .scoreHomeTeam(bet.getScoreHomeTeam())
                            .scoreAwayTeam(bet.getScoreAwayTeam())
                            .extraTime(bet.isExtraTime())
                            .penalty(bet.isPenalty())
                            .points(bet.getPoints())
                            .build();
                })
                .toList();
    }

    private TableRowDTO buildTableRowWithExtras(
            User user,
            double totalPoints,
            List<BetViewDTO> betViews,
            Map<Integer, ExtraPoints> extraPointsByUserId,
            Map<Integer, Team> teamsById
    ) {
        ExtraPoints extraPoints = extraPointsByUserId.get(user.getId());
        if (extraPoints == null) {
            throw new DataNotFoundException("Дополнительные очки для юзера с id=" + user.getId() + " не найдены");
            // todo для админа показывать страницу с логами ошибки, для юзера показвать страницу "что-то пошло не так"
        }

        String teamPrediction = EMPTY_WINNING_TEAM_PREDICTION;
        if (extraPoints.getWinningTeamId() > 0 && !teamsById.isEmpty()) {
            Team team = teamsById.get(extraPoints.getWinningTeamId());
            if (team == null) {
                throw new DataNotFoundException("Команда с id=" + extraPoints.getWinningTeamId() + " не найдена");
            }
            teamPrediction = team.getName();
        }

        return TableRowDTO.builder()
                .username(user.getUsername())
                .totalPointsForThisRound(totalPoints)
                .numberOfHitsOnCorrectScore(extraPoints.getNumberOfHitsOnCorrectScore())
                .numberOfHitsOnMatchResult(extraPoints.getNumberOfHitsOnMatchResult())
                .winningTeamPrediction(teamPrediction)
                .extraPoints(extraPoints.getExtraPoints())
                .predictAvailable(false)
                .betViews(betViews)
                .build();
    }

    private double getTotalPoints(TableRowDTO dto) {
        return dto.getTotalPointsForThisRound();
    }

    private Map<Integer, Double> getUserPointsByRound(int round) {
        List<Match> matches = getMatchesForRound(round);
        List<TableRowDTO> rows = buildTableRows(matches, round == ALL_STAGES_COMBINED);

        return rows.stream().collect(Collectors.toMap(
                row -> userRepository.findByUsername(row.getUsername())
                        .orElseThrow(() -> new DataNotFoundException("User not found: " + row.getUsername()))
                        .getId(),
                TableRowDTO::getTotalPointsForThisRound
        ));
    }

    private Map<Integer, Integer> computeRankings(Map<Integer, Double> userPointsMap) {
        List<Map.Entry<Integer, Double>> sorted = userPointsMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .toList();

        Map<Integer, Integer> ranks = new HashMap<>();
        int currentRank = 1;
        for (int i = 0; i < sorted.size(); i++) {
            if (i > 0 && Double.compare(sorted.get(i).getValue(), sorted.get(i - 1).getValue()) == 0) {
                ranks.put(sorted.get(i).getKey(), ranks.get(sorted.get(i - 1).getKey()));
            } else {
                ranks.put(sorted.get(i).getKey(), currentRank);
            }
            currentRank = i + 2;
        }

        return ranks;
    }
}
