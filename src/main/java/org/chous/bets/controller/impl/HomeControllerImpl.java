package org.chous.bets.controller.impl;

import org.chous.bets.controller.HomeControllerAPI;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class HomeControllerImpl implements HomeControllerAPI {

    @Override
    public String getHome(Model model) {
        return "home";
    }

    @Override
    public String getRules(Model model) {
        return "rules";
    }
}
