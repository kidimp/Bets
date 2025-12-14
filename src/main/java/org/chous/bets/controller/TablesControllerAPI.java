package org.chous.bets.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// todo в будущем лучше сделать динамический контроллер с параметрами: tables/{tournamentId}/{stage}

@Tag(name = "Tournament Tables", description = "Отображение таблиц турнира: этапы, суммарные результаты и лидерборд")
@RequestMapping("/tables")
public interface TablesControllerAPI {

    @Operation(
            summary = "Таблица первого тура",
            description = "Возвращает страницу с результатами первого тура"
    )
    @GetMapping("/first-round")
    String getFirstRound(Model model);

    @Operation(
            summary = "Таблица второго тура",
            description = "Возвращает страницу с результатами второго тура"
    )
    @GetMapping("/second-round")
    String getSecondRound(Model model);

    @Operation(
            summary = "Таблица третьего тура",
            description = "Возвращает страницу с результатами третьего тура"
    )
    @GetMapping("/third-round")
    String getThirdRound(Model model);

    @Operation(
            summary = "Таблица стадии плей-офф",
            description = "Возвращает страницу с результатами стадии knockout"
    )
    @GetMapping("/knockout-stage")
    String getKnockoutStage(Model model);

    @Operation(
            summary = "Суммарный счёт по стадиям",
            description = "Возвращает страницу с объединённой таблицей результатов по всем стадиям"
    )
    @GetMapping("/stage-total")
    String getStagesCombinedScore(Model model);

    @Operation(
            summary = "Лидерборд турнира",
            description = "Возвращает страницу с итоговым лидербордом турнира"
    )
    @GetMapping
    String getTournamentLeaderboard(Model model);
}
