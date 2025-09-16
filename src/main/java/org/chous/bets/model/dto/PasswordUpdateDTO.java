package org.chous.bets.model.dto;

import jakarta.validation.constraints.NotBlank;
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
public class PasswordUpdateDTO {

    @NotBlank
    @Size(min = 3, message = "Пароль должен состоять не менее чем из трех символов")
    private String password;

    @NotBlank
    @Size(min = 3, message = "Пароль должен состоять не менее чем из трех символов")
    private String passwordConfirm;
}
