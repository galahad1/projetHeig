package models;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author Lassalle Loan
 * @since 8/03/2017
 */

@Entity
@Table(name = "RubriqueEnfant")
public class RubriqueEnfant {

    @Id
    @Column(name = "idRubriqueEnfant")
    private int idRubriqueEnfant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idRubriqueParent")
    private int idRubriqueParent;

    @Column(name = "nomRubriqueEnfant", length = 20)
    private String nomRubriqueEnfant;

    @Column(name = "derniereMiseAJour", length = 40)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar derniereMiseAJour;

    public RubriqueEnfant() {
    }

    public RubriqueEnfant(int idRubriqueEnfant, int idRubriqueParent, String nomRubriqueEnfant,
                          Calendar derniereMiseAJour) {
        this.idRubriqueEnfant = idRubriqueEnfant;
        this.idRubriqueParent = idRubriqueParent;
        this.nomRubriqueEnfant = nomRubriqueEnfant;
        this.derniereMiseAJour = derniereMiseAJour;
    }

    public int getIdRubriqueEnfant() {
        return idRubriqueEnfant;
    }

    public void setIdRubriqueEnfant(int idRubriqueEnfant) {
        this.idRubriqueEnfant = idRubriqueEnfant;
    }

    public int getIdRubriqueParent() {
        return idRubriqueParent;
    }

    public void setIdRubriqueParent(int idRubriqueParent) {
        this.idRubriqueParent = idRubriqueParent;
    }

    public String getNomRubriqueEnfant() {
        return nomRubriqueEnfant;
    }

    public void setNomRubriqueEnfant(String nomRubriqueEnfant) {
        this.nomRubriqueEnfant = nomRubriqueEnfant;
    }

    public Calendar getDerniereMiseAJour() {
        return derniereMiseAJour;
    }

    public void setDerniereMiseAJour(Calendar derniereMiseAJour) {
        this.derniereMiseAJour = derniereMiseAJour;
    }

    @Override
    public String toString() {
        return "RubriqueEnfant{" +
                "idRubriqueEnfant=" + idRubriqueEnfant +
                ", idRubriqueParent=" + idRubriqueParent +
                ", nomRubriqueEnfant='" + nomRubriqueEnfant + '\'' +
                ", derniereMiseAJour=" + derniereMiseAJour +
                '}';
    }
}
