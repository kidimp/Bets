package org.chous.bets.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/fixtures")
    public String fixtures() {
        return "fixtures";
    }

    @GetMapping("/tables")
    public String tables() {
        return "tables";
    }

}
