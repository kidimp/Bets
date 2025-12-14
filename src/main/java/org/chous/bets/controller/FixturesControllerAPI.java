package org.chous.bets.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Fixtures", description = "Просмотр расписания матчей")
public interface FixturesControllerAPI {

    @Operation(
            summary = "Страница расписания матчей",
            description = "Возвращает страницу с отображением всех предстоящих матчей"
    )
    @GetMapping("/fixtures")
    String getFixtures(Model model);
}
