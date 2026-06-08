package org.chous.bets.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chous.bets.exception.DataNotFoundException;
import org.chous.bets.mapper.BetMapper;
import org.chous.bets.model.dto.BetDTO;
import org.chous.bets.model.entity.Bet;
import org.chous.bets.model.entity.ExtraPoints;
import org.chous.bets.model.entity.Match;
import org.chous.bets.model.entity.User;
import org.chous.bets.repository.BetRepository;
import org.chous.bets.repository.ExtraPointsRepository;
import org.chous.bets.repository.MatchRepository;
import org.chous.bets.repository.UserRepository;
import org.chous.bets.service.BetService;
import org.chous.bets.service.UserService;
import org.chous.bets.util.SecurityContextUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BetServiceImpl implements BetService {

    private final UserService userService;
    private final BetRepository betRepository;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final ExtraPointsRepository extraPointsRepository;
    private final BetMapper betMapper;

    @Override
    public Bet getOrCreateBetForUserAndMatch(int userId, int matchId) {
        return betRepository.findByUserIdAndMatchId(userId, matchId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
                    Match match = matchRepository.findById(matchId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found"));

                    Bet bet = new Bet();
                    bet.setUser(user);
                    bet.setMatch(match);
                    return bet;
                });
    }

    @Override
    public List<BetDTO> getBets() {
        String email = SecurityContextUtil.getPrincipal();
        int userId = userService.getPrincipalUserId(email);

        List<Bet> userBets = betRepository.findByUserId(userId);
        return userBets.stream()
                .map(betMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void saveOrUpdateBet(int userId, int matchId, BetDTO betDTO) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new DataNotFoundException("Команда с id=" + matchId + " не найдена"));

        if (LocalDateTime.now().isAfter(match.getDateAndTime())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нельзя делать ставки после начала матча");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь с id=" + userId + " не найден."));

        Optional<Bet> existingBetOpt = betRepository.findByUserIdAndMatchId(userId, matchId);
        Bet bet = betMapper.toEntity(betDTO);
        bet.setUser(user);
        bet.setMatch(match);

        existingBetOpt.ifPresent(existingBet -> bet.setId(existingBet.getId()));

        // Проверяем, есть ли запись пользователя в таблице extra_points, и если нет, то создаём дефолтную
        createDefaultExtraPointsIfAbsent(userId);

        betRepository.save(bet);
    }

    private void createDefaultExtraPointsIfAbsent(int userId) {
        boolean isExtraPoints = extraPointsRepository.existsByUserId(userId);

        if (!isExtraPoints) {

            ExtraPoints extraPoints = new ExtraPoints();
            extraPoints.setUserId(userId);
            extraPoints.setWinningTeamId(0);
            extraPoints.setNumberOfHitsOnCorrectScore(0);
            extraPoints.setNumberOfHitsOnMatchResult(0);
            extraPoints.setExtraPoints(0.0);

            extraPointsRepository.save(extraPoints);
        }
    }
}
