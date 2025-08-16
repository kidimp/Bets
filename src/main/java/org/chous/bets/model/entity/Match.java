package org.chous.bets.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "date_and_time", nullable = false)
    private LocalDateTime dateAndTime;

    @NotNull
    @Column(name = "stage_id", nullable = false)
    private Integer stageId;

    @NotNull
    @Column(name = "round_code", nullable = false)
    private Integer round;

    @NotNull
    @Column(name = "home_team_id", nullable = false)
    private Integer homeTeamId;

    @NotNull
    @Column(name = "away_team_id", nullable = false)
    private Integer awayTeamId;

    @Column(name = "is_finished", nullable = false)
    private boolean finished;

    @Min(value = 0, message = "Value cannot be negative")
    @Column(name = "score_home_team")
    private int scoreHomeTeam;

    @Min(value = 0, message = "Value cannot be negative")
    @Column(name = "score_away_team")
    private int scoreAwayTeam;

    @Column(name = "is_extra_time", nullable = false)
    private boolean extraTime;

    //todo привести к единому виду все boolean переменные
    @Column(name = "is_penalty", nullable = false)
    private boolean penalty;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o.getClass() != this.getClass()) return false;

        Class<?> thisClass = this instanceof HibernateProxy proxy
                ? proxy.getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();

        Class<?> otherClass = o instanceof HibernateProxy proxy
                ? proxy.getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();

        if (!thisClass.equals(otherClass)) return false;

        Match match = (Match) o;
        return id != null && Objects.equals(id, match.id);
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy
                ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
