package org.chous.bets.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tournament")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 3, max = 20, message = "Code must be between 3 and 20 characters")
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters")
    @Column(name = "name", nullable = false, length = 30)
    private String name;

    // todo только один турнир может быть активным
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @NotNull
    @Column(name = "start", nullable = false)
    private LocalDateTime start;

    @NotNull
    @Column(name = "finish", nullable = false)
    private LocalDateTime finish;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tournament_team",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private Set<Team> teams = new HashSet<>();

//    @ToString.Exclude
//    private Set<Stage> stages;
//
//    @ToString.Exclude
//    private Set<Round> rounds;
//
//    @ToString.Exclude
//    private Set<User> users;

    @Column(name = "winningTeamId")
    private Integer winningTeamId;

    @Column(name = "runnerUpTeamId")
    private Integer runnerUpTeamId;


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

        Tournament that = (Tournament) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy
                ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
