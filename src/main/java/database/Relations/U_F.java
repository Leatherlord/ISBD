package database.Relations;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "\"U_F+\"", schema = "s312421")
public class U_F {

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
    @JoinColumn(name = "f_id")
    private Fraction fraction;

    @Getter
    @Setter
    @Column(name = "since")
    private Date since;

    @Getter
    @Setter
    @Column(name = "until")
    private Date until;
}
