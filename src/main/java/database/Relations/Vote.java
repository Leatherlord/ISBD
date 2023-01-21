package database.Relations;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "\"Vote\"", schema = "s312421")
public class Vote {

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
    @JoinColumn(name = "v_iter_id")
    private Voting_Iteration votingIteration;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "appl_id")
    private Application application;

    @Getter
    @Setter
    @Column(name = "date")
    private Date date;
}
