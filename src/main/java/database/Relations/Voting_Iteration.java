package database.Relations;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"Voting_Iteration\"", schema = "s312421")
public class Voting_Iteration {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name = "since")
    private Date since;

    @Getter
    @Setter
    @Column(name = "until")
    private Date until;

    @Getter
    @Setter
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "votingIteration", cascade = CascadeType.ALL)
    private Set<Vote> votes = new HashSet<>();
}
