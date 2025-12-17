package org.chous.bets.controller.admin.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.controller.admin.PointsControllerAPI;
import org.chous.bets.service.PointsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class PointsControllerImpl implements PointsControllerAPI {

    private final PointsService pointsService;

    @Override
    public String recalculateTables() {
        pointsService.recalculatePoints();
        return "redirect:/admin/matches";
    }
}
