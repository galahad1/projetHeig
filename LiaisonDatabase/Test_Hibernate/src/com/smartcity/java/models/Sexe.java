package models;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author Lassalle Loan
 * @since 8/03/2017
 */

@Entity
@Table(name = "Sexe")
public class Sexe {

    @Id
    @Column(name = "idSexe")
    private int idSexe;

    @Column(name = "nomSexe", length = 45)
    private String nomSexe;

    @Column(name = "derniereMiseAJour", length = 40)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar derniereMiseAJour;

    public Sexe() {
    }

    public Sexe(int idSexe, String nomSexe, Calendar derniereMiseAJour) {
        this.idSexe = idSexe;
        this.nomSexe = nomSexe;
        this.derniereMiseAJour = derniereMiseAJour;
    }

    public int getIdSexe() {
        return idSexe;
    }

    public void setIdSexe(int idSexe) {
        this.idSexe = idSexe;
    }

    public String getNomSexe() {
        return nomSexe;
    }

    public void setNomSexe(String nomSexe) {
        this.nomSexe = nomSexe;
    }

    public Calendar getDerniereMiseAJour() {
        return derniereMiseAJour;
    }

    public void setDerniereMiseAJour(Calendar derniereMiseAJour) {
        this.derniereMiseAJour = derniereMiseAJour;
    }

    @Override
    public String toString() {
        return "Sexe{" +
                "idSexe=" + idSexe +
                ", nomSexe='" + nomSexe + '\'' +
                ", derniereMiseAJour=" + derniereMiseAJour +
                '}';
    }
}
