package org.chous.bets.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Archive", description = "Просмотр прошедших турниров (фича будет дорабатываться)")
@RequestMapping("/archive")
public interface ArchiveControllerAPI {

    @Operation(
            summary = "Список прошедших турниров",
            description = "Возвращает страницу со всеми прошедшими турнирами"
    )
    @GetMapping
    String getAllTournaments(Model model);

    @Operation(
            summary = "Первый тур EURO 2024",
            description = "Возвращает страницу с матчами первого тура EURO 2024"
    )
    @GetMapping("/euro2024-first-round")
    String getFirstRound(Model model);

    @Operation(
            summary = "Второй тур EURO 2024",
            description = "Возвращает страницу с матчами второго тура EURO 2024"
    )
    @GetMapping("/euro2024-second-round")
    String getSecondRound(Model model);

    @Operation(
            summary = "Третий тур EURO 2024",
            description = "Возвращает страницу с матчами третьего тура EURO 2024"
    )
    @GetMapping("/euro2024-third-round")
    String getThirdRound(Model model);

    @Operation(
            summary = "Плей-офф EURO 2024",
            description = "Возвращает страницу стадии плей-офф EURO 2024"
    )
    @GetMapping("/euro2024-knockout-stage")
    String getKnockoutStage(Model model);

    @Operation(
            summary = "Суммарный счёт по всем стадиям",
            description = "Возвращает сводную таблицу результатов по стадиям EURO 2024"
    )
    @GetMapping("/euro2024-stage-total")
    String getStagesCombinedScore(Model model);

    @Operation(
            summary = "Таблица лидеров турнира",
            description = "Отображает leaderboard по итогам EURO 2024"
    )
    @GetMapping("/euro2024-leaderboard")
    String getTournamentLeaderboard(Model model);
}
