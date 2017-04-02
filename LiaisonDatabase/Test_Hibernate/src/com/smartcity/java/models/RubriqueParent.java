package models;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author Lassalle Loan
 * @since 8/03/2017
 */

@Entity
@Table(name = "RubriqueParent")
public class RubriqueParent {

    @Id
    @Column(name = "idRubriqueParent")
    private int idRubriqueParent;

    @Column(name = "nomRubriqueParent", length = 20)
    private String nomRubriqueParent;

    @Column(name = "derniereMiseAJour", length = 40)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar derniereMiseAJour;

    public RubriqueParent() {
    }

    public RubriqueParent(int idRubriqueParent, String nomRubriqueParent, Calendar derniereMiseAJour) {
        this.idRubriqueParent = idRubriqueParent;
        this.nomRubriqueParent = nomRubriqueParent;
        this.derniereMiseAJour = derniereMiseAJour;
    }

    public int getIdRubriqueParent() {
        return idRubriqueParent;
    }

    public void setIdRubriqueParent(int idRubriqueParent) {
        this.idRubriqueParent = idRubriqueParent;
    }

    public String getNomRubriqueParent() {
        return nomRubriqueParent;
    }

    public void setNomRubriqueParent(String nomRubriqueParent) {
        this.nomRubriqueParent = nomRubriqueParent;
    }

    public Calendar getDerniereMiseAJour() {
        return derniereMiseAJour;
    }

    public void setDerniereMiseAJour(Calendar derniereMiseAJour) {
        this.derniereMiseAJour = derniereMiseAJour;
    }

    @Override
    public String toString() {
        return "RubriqueParent{" +
                "idRubriqueParent=" + idRubriqueParent +
                ", nomRubriqueParent='" + nomRubriqueParent + '\'' +
                ", derniereMiseAJour=" + derniereMiseAJour +
                '}';
    }
}
