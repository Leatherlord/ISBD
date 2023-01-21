package database.Relations;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"Application\"", schema = "s312421")
public class Application {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "u_id")
    private User user;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "pos_id")
    private Position position;

    @Getter
    @Setter
    @Column(name = "date")
    private Date date;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "f_id")
    private Fraction fraction;

    @Getter
    @Setter
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "application", cascade = CascadeType.ALL)
    private Set<Vote> votes = new HashSet<>();

}