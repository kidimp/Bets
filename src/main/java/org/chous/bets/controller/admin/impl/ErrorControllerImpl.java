package org.chous.bets.controller.admin.impl;

import io.swagger.v3.oas.annotations.Operation;
import org.chous.bets.controller.admin.ErrorController;
import org.springframework.stereotype.Controller;

@Controller
public class ErrorControllerImpl implements ErrorController {

    @Operation(
            summary = "Страница отказа в доступе (403)",
            description = "Отображает страницу с сообщением об отсутствии прав доступа к запрашиваемому ресурсу"
    )
    public String accessDenied() {
        return "error/403";
    }
}
