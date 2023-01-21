package database.Relations;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"Fraction\"", schema = "s312421")
public class Fraction {

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
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fraction", cascade = CascadeType.ALL)
    private Set<Application> applications = new HashSet<>();

    @Getter
    @Setter
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "fraction", cascade = CascadeType.ALL)
    private Set<U_F> u_fSet = new HashSet<>();

    @Getter
    @Setter
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "fraction", cascade = CascadeType.ALL)
    private Set<U_P> u_pSet = new HashSet<>();
}
