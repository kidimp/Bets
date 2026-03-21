package org.chous.bets.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class StageDTO {

    private Integer id;

    @NotEmpty(message = "Название не должно быть пустым")
    @Size(min = 3, max = 30, message = "Название должно быть от 3 до 30 символов")
    private String name;

    private boolean isKnockoutStage;
}
