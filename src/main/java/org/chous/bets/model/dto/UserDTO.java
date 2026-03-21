package org.chous.bets.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.chous.bets.model.enums.RoleEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserDTO {

    private Integer id;

    @NotBlank
    @Size(min = 2, max = 100)
    private String username;

    @Email
    @NotBlank
    private String email;

    private RoleEnum role;

    private boolean isActive;
}
