package org.chous.bets.controller;

import org.springframework.web.bind.annotation.PostMapping;

public interface PointsControllerAPI {

    @PostMapping("/admin/recalculate-tables")
    String recalculate();
}
