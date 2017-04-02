package models;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author Lassalle Loan
 * @since 8/03/2017
 */

@Entity
@Table(name = "Npa")
public class Npa {

    @Id
    @Column(name = "idNpa")
    private int idNpa;

    @Column(name = "numeroNpa", length = 4)
    private String numeroNpa;

    @Column(name = "derniereMiseAJour", length = 40)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar derniereMiseAJour;

    public Npa() {

    }

    public Npa(int idNpa, String numeroNpa, Calendar derniereMiseAJour) {
        this.idNpa = idNpa;
        this.numeroNpa = numeroNpa;
        this.derniereMiseAJour = derniereMiseAJour;
    }

    public int getIdNpa() {
        return idNpa;
    }

    public void setIdNpa(int idNpa) {
        this.idNpa = idNpa;
    }

    public String getNumeroNpa() {
        return numeroNpa;
    }

    public void setNumeroNpa(String numeroNpa) {
        this.numeroNpa = numeroNpa;
    }

    public Calendar getDerniereMiseAJour() {
        return derniereMiseAJour;
    }

    public void setDerniereMiseAJour(Calendar derniereMiseAJour) {
        this.derniereMiseAJour = derniereMiseAJour;
    }

    @Override
    public String toString() {
        return "Npa{" +
                "idNpa=" + idNpa +
                ", numeroNpa='" + numeroNpa + '\'' +
                ", derniereMiseAJour=" + derniereMiseAJour.getTime() +
                '}';
    }
}
