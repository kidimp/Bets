package org.chous.bets.controller.user.impl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Error Controller")
@Controller
public class ErrorControllerImpl {

    @Operation(
            summary = "Страница отказа в доступе (403)",
            description = "Отображает страницу с сообщением об отсутствии прав доступа к запрашиваемому ресурсу"
    )
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/403";
    }
}
