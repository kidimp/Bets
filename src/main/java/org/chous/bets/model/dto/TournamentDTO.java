package org.chous.bets.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.chous.bets.model.entity.Team;
import org.chous.bets.validator.UniqueTeams;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class TournamentDTO {

    private Integer id;

    @NotBlank
    @Size(min = 3, max = 20)
    private String code;

    @NotBlank
    @Size(min = 3, max = 30)
    private String name;

    @NotNull(message = "Дата и время старта турнира обязательны")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime start;

    @NotNull(message = "Дата и время окончания турнира обязательны")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime finish;

    @UniqueTeams
    @Schema(name = "teams", description = "Команды, принимающие участие в турнире")
    private @Valid Set<@Valid TeamDTO> teams;
}
