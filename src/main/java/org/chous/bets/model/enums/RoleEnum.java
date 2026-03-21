package org.chous.bets.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {

    ADMIN("ADMIN"),
    USER("USER");

    @JsonValue
    private final String role;
}
