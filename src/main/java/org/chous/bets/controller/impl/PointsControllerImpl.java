package org.chous.bets.controller.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.controller.PointsControllerAPI;
import org.chous.bets.service.PointsServiceAPI;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class PointsControllerImpl implements PointsControllerAPI {

    private final PointsServiceAPI pointsService;

    @Override
    public String recalculate() {
        pointsService.recalculatePoints();
        return "redirect:/admin/matches";
    }
}
