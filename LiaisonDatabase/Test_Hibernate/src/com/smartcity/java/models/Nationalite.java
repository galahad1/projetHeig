package models;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author Lassalle Loan
 * @since 8/03/2017
 */

@Entity
@Table(name = "Nationalite")
public class Nationalite {

    @Id
    @Column(name = "idNationalite")
    private int idNationalite;

    @Column(name = "nomNationalite")
    private String nomNationalite;

    @Column(name = "derniereMiseAJour", length = 40)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar derniereMiseAJour;

    public Nationalite() {
    }

    public Nationalite(int idNationalite, String nomNationalite, Calendar derniereMiseAJour) {
        this.idNationalite = idNationalite;
        this.nomNationalite = nomNationalite;
        this.derniereMiseAJour = derniereMiseAJour;
    }

    public int getIdNationalite() {
        return idNationalite;
    }

    public void setIdNationalite(int idNationalite) {
        this.idNationalite = idNationalite;
    }

    public String getNomNationalite() {
        return nomNationalite;
    }

    public void setNomNationalite(String nomNationalite) {
        this.nomNationalite = nomNationalite;
    }

    public Calendar getDerniereMiseAJour() {
        return derniereMiseAJour;
    }

    public void setDerniereMiseAJour(Calendar derniereMiseAJour) {
        this.derniereMiseAJour = derniereMiseAJour;
    }

    @Override
    public String toString() {
        return "Nationalite{" +
                "idNationalite=" + idNationalite +
                ", nomNationalite='" + nomNationalite + '\'' +
                ", derniereMiseAJour=" + derniereMiseAJour +
                '}';
    }
}
