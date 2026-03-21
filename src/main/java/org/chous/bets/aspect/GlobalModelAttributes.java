package org.chous.bets.aspect;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @Value("${tournament-name}")
    private String tournamentName;

    @ModelAttribute("tournamentName")
    public String tournamentName() {
        return tournamentName;
    }
}
//todo добавить изменение названия турнира в админку с сохранением в бд, сейчас через .env
