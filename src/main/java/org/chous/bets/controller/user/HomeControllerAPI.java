package org.chous.bets.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Home", description = "Главная страница и правила проекта")
public interface HomeControllerAPI {

    @Operation(
            summary = "Главная страница",
            description = "Возвращает главную страницу сайта"
    )
    @GetMapping("/")
    String getHome(Model model);

    @Operation(
            summary = "Правила проекта",
            description = "Возвращает страницу с правилами участия / использования сервиса"
    )
    @GetMapping("/rules")
    String getRules(Model model);
}
