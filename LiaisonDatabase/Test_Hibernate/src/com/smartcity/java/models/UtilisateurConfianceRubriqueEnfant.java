package models;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author Lassalle Loan
 * @since 8/03/2017
 */

@Entity
@Table(name = "UtilisateurConfianceRubriqueEnfant")
public class UtilisateurConfianceRubriqueEnfant {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUtilisateur")
    private int idUtilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idRubriqueEnfant")
    private int idRubriqueEnfant;

    @Column(name = "creation", length = 40)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar creation;

    public UtilisateurConfianceRubriqueEnfant() {
    }

    public UtilisateurConfianceRubriqueEnfant(int idUtilisateur, int idRubriqueEnfant, Calendar creation) {
        this.idUtilisateur = idUtilisateur;
        this.idRubriqueEnfant = idRubriqueEnfant;
        this.creation = creation;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public int getIdRubriqueEnfant() {
        return idRubriqueEnfant;
    }

    public void setIdRubriqueEnfant(int idRubriqueEnfant) {
        this.idRubriqueEnfant = idRubriqueEnfant;
    }

    public Calendar getCreation() {
        return creation;
    }

    public void setCreation(Calendar creation) {
        this.creation = creation;
    }

    @Override
    public String toString() {
        return "UtilisateurConfianceRubriqueEnfant{" +
                "idUtilisateur=" + idUtilisateur +
                ", idRubriqueEnfant=" + idRubriqueEnfant +
                ", creation=" + creation +
                '}';
    }
}
