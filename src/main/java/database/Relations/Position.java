package database.Relations;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"Position\"", schema = "s312421")
public class Position {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name = "name")
    private String name;

    @Getter
    @Setter
    @Column(name = "level")
    private String level;

    @Getter
    @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "position", cascade = CascadeType.ALL)
    private Set<Application> applications = new HashSet<>();

    @Getter
    @Setter
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "position", cascade = CascadeType.ALL)
    private Set<U_P> u_pSet = new HashSet<>();
}
