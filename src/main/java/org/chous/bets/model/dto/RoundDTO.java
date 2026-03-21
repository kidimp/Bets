package org.chous.bets.model.dto;

import jakarta.validation.constraints.Min;
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
public class RoundDTO {

    private Integer id;

    @NotEmpty(message = "Название не должно быть пустым")
    @Size(max = 256, message = "Название не должно превышать 256 символов")
    private String name;

    @Min(value = 1, message = "Номер раунда должен быть не менее 1")
    private int round;
}
