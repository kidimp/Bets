package org.chous.bets.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Comparator;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teams", uniqueConstraints = {
        @UniqueConstraint(name = "unq_team_iso_name", columnNames = {"iso_name"})
})
public class Team implements Comparable<Team> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters")
    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @NotEmpty(message = "ISO name must not be empty")
    @Size(min = 2, max = 2, message = "ISO name must be exactly 2 characters")
    @Column(name = "iso_name", nullable = false, length = 2, unique = true)
    private String isoName;

    @Min(value = 1, message = "Pot must be from 1 to 4")
    @Max(value = 4, message = "Pot must be from 1 to 4")
    @Column(name = "pot", nullable = false)
    private int pot;

    @Override
    public int compareTo(Team team) {
        return Comparators.NAME.compare(this, team);
    }

    public static class Comparators {
        public static final Comparator<Team> NAME = Comparator.comparing(Team::getName);
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o.getClass() != this.getClass()) return false;

        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy
                ? proxy.getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy
                ? proxy.getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (!thisEffectiveClass.equals(oEffectiveClass)) return false;

        Team team = (Team) o;
        return getId() != null && Objects.equals(getId(), team.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy
                ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}