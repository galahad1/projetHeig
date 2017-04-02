package models;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author Lassalle Loan
 * @since 8/03/2017
 */

@Entity
@Table(name = "Rue")
public class Rue {

    @Column(name = "derniereMiseAJour", length = 40)
    @Temporal(TemporalType.TIMESTAMP)
    Calendar derniereMiseAJour;
    @Id
    @Column(name = "idRue")
    private int idRue;
    @Column(name = "rue", length = 45)
    private String rue;

    public Rue() {
    }

    public Rue(int idRue, String rue, Calendar derniereMiseAJour) {
        this.idRue = idRue;
        this.rue = rue;
        this.derniereMiseAJour = derniereMiseAJour;
    }

    public int getIdRue() {
        return idRue;
    }

    public void setIdRue(int idRue) {
        this.idRue = idRue;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public Calendar getDerniereMiseAJour() {
        return derniereMiseAJour;
    }

    public void setDerniereMiseAJour(Calendar derniereMiseAJour) {
        this.derniereMiseAJour = derniereMiseAJour;
    }

    @Override
    public String toString() {
        return "Rue{" +
                "idRue=" + idRue +
                ", rue='" + rue + '\'' +
                ", derniereMiseAJour=" + derniereMiseAJour +
                '}';
    }
}
