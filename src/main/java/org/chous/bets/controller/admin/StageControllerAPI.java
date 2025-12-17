package org.chous.bets.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.chous.bets.model.dto.StageDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Admin Stages", description = "Админские операции для работы со стадиями турниров")
@RequestMapping("/admin/stages")
public interface StageControllerAPI {

    @Operation(
            summary = "Список всех стадий",
            description = "Возвращает страницу со всеми стадиями"
    )
    @GetMapping
    String getAllStages(Model model);

    @Operation(
            summary = "Форма создания стадии",
            description = "Возвращает страницу с формой для создания новой стадии"
    )
    @GetMapping("/new")
    String createStageForm(
            @Parameter(description = "DTO новой стадии")
            @ModelAttribute("stage") StageDTO stageDTO,
            Model model
    );

    @Operation(
            summary = "Создание новой стадии",
            description = "Обрабатывает форму создания стадии. В случае ошибок валидации возвращает форму."
    )
    @PostMapping("/new")
    String createStage(
            @Parameter(description = "Данные создаваемой стадии")
            @ModelAttribute("stage") @Valid StageDTO stageDTO,
            BindingResult bindingResult
    );

    @Operation(
            summary = "Форма редактирования стадии",
            description = "Отображает форму редактирования стадии по ID"
    )
    @GetMapping("/{id}/edit")
    String editStageForm(
            @Parameter(description = "ID стадии")
            @PathVariable("id") Integer id,
            Model model
    );

    @Operation(
            summary = "Обновление стадии",
            description = "Обрабатывает форму редактирования стадии"
    )
    @PostMapping("/{id}/edit")
    String updateStage(
            @Parameter(description = "ID стадии для обновления")
            @PathVariable("id") Integer id,
            @Parameter(description = "Обновлённые данные стадии")
            @ModelAttribute("stage") @Valid StageDTO stageDTO,
            BindingResult bindingResult
    );

    @Operation(
            summary = "Удаление стадии",
            description = "Удаляет стадию по ID"
    )
    @PostMapping("/{id}/delete")
    String delete(
            @Parameter(description = "ID стадии для удаления")
            @PathVariable("id") Integer id
    );
}
