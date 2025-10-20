package org.chous.bets.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public interface HomeControllerAPI {

    @GetMapping(value = "/")
    String home(Model model);

    @GetMapping("/rules")
    String rules(Model model);
}
