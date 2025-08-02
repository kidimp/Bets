package org.chous.bets.model.dto;

import jakarta.validation.constraints.Max;
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
public class TeamDTO {

    private Integer id;

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters")
    private String name;

    @NotEmpty(message = "ISO name must not be empty")
    @Size(min = 2, max = 2, message = "ISO name must be exactly 2 characters")
    private String isoName;

    @Min(value = 1, message = "Pot must be from 1 to 4")
    @Max(value = 4, message = "Pot must be from 1 to 4")
    private int pot;
}
