package org.chous.bets.controller.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Error Controller")
public interface ErrorController {

    @GetMapping("/access-denied")
    String accessDenied();
}
