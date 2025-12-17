package org.chous.bets.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Admin Points", description = "Админские операции, связанные с пересчётом")
@RequestMapping("/admin")
public interface PointsControllerAPI {

    @Operation(
            summary = "Пересчёт турнирных таблиц",
            description = "Запускает пересчёт таблиц/очков. Используется только администраторами."
    )
    @PostMapping("/recalculate-tables")
    String recalculateTables();
}
