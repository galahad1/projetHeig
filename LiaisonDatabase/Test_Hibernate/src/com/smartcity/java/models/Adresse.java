package models;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author Lassalle Loan
 * @since 8/03/2017
 */

@Entity
@Table(name = "Adresse")
public class Adresse {

    @Id
    @Column(name = "idAdresse")
    private int idAdresse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idRue")
    private int idRue;

    @Column(name = "numeroDeRue", length = 4)
    private String numeroDeRue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idNpa")
    private int idNpa;

    @Column(name = "derniereMiseAJour", length = 40)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar derniereMiseAJour;

    public Adresse() {
    }

    public Adresse(int idAdresse, int idRue, String numeroDeRue, int idNpa, Calendar derniereMiseAJour) {
        this.idAdresse = idAdresse;
        this.idRue = idRue;
        this.numeroDeRue = numeroDeRue;
        this.idNpa = idNpa;
        this.derniereMiseAJour = derniereMiseAJour;
    }

    public int getIdAdresse() {
        return idAdresse;
    }

    public void setIdAdresse(int idAdresse) {
        this.idAdresse = idAdresse;
    }

    public int getIdRue() {
        return idRue;
    }

    public void setIdRue(int idRue) {
        this.idRue = idRue;
    }

    public String getNumeroDeRue() {
        return numeroDeRue;
    }

    public void setNumeroDeRue(String numeroDeRue) {
        this.numeroDeRue = numeroDeRue;
    }

    public int getIdNpa() {
        return idNpa;
    }

    public void setIdNpa(int idNpa) {
        this.idNpa = idNpa;
    }

    public Calendar getDerniereMiseAJour() {
        return derniereMiseAJour;
    }

    public void setDerniereMiseAJour(Calendar derniereMiseAJour) {
        this.derniereMiseAJour = derniereMiseAJour;
    }

    @Override
    public String toString() {
        return "Adresse{" +
                "idAdresse=" + idAdresse +
                ", idRue=" + idRue +
                ", numeroDeRue='" + numeroDeRue + '\'' +
                ", idNpa=" + idNpa +
                ", derniereMiseAJour=" + derniereMiseAJour +
                '}';
    }
}
