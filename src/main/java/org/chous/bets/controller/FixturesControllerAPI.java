package org.chous.bets.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public interface FixturesControllerAPI {

    @GetMapping("/fixtures")
    String fixtures(Model model);
}
