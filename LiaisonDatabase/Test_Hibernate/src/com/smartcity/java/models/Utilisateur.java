package models;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author Lassalle Loan
 * @since 8/03/2017
 */

@Entity
@Table(name = "Utilisateur")
public class Utilisateur {

    @Id
    @Column(name = "idUtilisateur")
    private int idUtilisateur;

    @Column(name = "personnePhysique")
    private Boolean personnePhysique;

    @Column(name = "avs", length = 16)
    private String avs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idTitreCivil")
    private int idTitreCivil;

    @Column(name = "nomUtilisateur", length = 90)
    private String nomUtilisateur;

    @Column(name = "prenom", length = 90)
    private String prenom;

    @Column(name = "dateNaissance", length = 40)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar dateNaissance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSexe")
    private int idSexe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idNationalite")
    private int idNationalite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAdresse")
    private int idAdresse;

    @Column(name = "email", length = 254)
    private String email;

    @Column(name = "pseudo", length = 90)
    private String pseudo;

    @Column(name = "motDePasse", length = 32)
    private String motDePasse;

    @Column(name = "sel", length = 32)
    private String sel;

    @Column(name = "creation", length = 40)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar creation;

    @Column(name = "derniereMiseAJour", length = 40)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar derniereMiseAJour;

    public Utilisateur() {
    }

    public Utilisateur(int idUtilisateur, Boolean personnePhysique, String avs, int idTitreCivil,
                       String nomUtilisateur, String prenom, Calendar dateNaissance, int idSexe, int idNationalite,
                       int idAdresse, String email, String pseudo, String motDePasse, String sel, Calendar creation,
                       Calendar derniereMiseAJour) {
        this.idUtilisateur = idUtilisateur;
        this.personnePhysique = personnePhysique;
        this.avs = avs;
        this.idTitreCivil = idTitreCivil;
        this.nomUtilisateur = nomUtilisateur;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.idSexe = idSexe;
        this.idNationalite = idNationalite;
        this.idAdresse = idAdresse;
        this.email = email;
        this.pseudo = pseudo;
        this.motDePasse = motDePasse;
        this.sel = sel;
        this.creation = creation;
        this.derniereMiseAJour = derniereMiseAJour;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public Boolean getPersonnePhysique() {
        return personnePhysique;
    }

    public void setPersonnePhysique(Boolean personnePhysique) {
        this.personnePhysique = personnePhysique;
    }

    public String getAvs() {
        return avs;
    }

    public void setAvs(String avs) {
        this.avs = avs;
    }

    public int getIdTitreCivil() {
        return idTitreCivil;
    }

    public void setIdTitreCivil(int idTitreCivil) {
        this.idTitreCivil = idTitreCivil;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Calendar getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Calendar dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public int getIdSexe() {
        return idSexe;
    }

    public void setIdSexe(int idSexe) {
        this.idSexe = idSexe;
    }

    public int getIdNationalite() {
        return idNationalite;
    }

    public void setIdNationalite(int idNationalite) {
        this.idNationalite = idNationalite;
    }

    public int getIdAdresse() {
        return idAdresse;
    }

    public void setIdAdresse(int idAdresse) {
        this.idAdresse = idAdresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getSel() {
        return sel;
    }

    public void setSel(String sel) {
        this.sel = sel;
    }

    public Calendar getCreation() {
        return creation;
    }

    public void setCreation(Calendar creation) {
        this.creation = creation;
    }

    public Calendar getDerniereMiseAJour() {
        return derniereMiseAJour;
    }

    public void setDerniereMiseAJour(Calendar derniereMiseAJour) {
        this.derniereMiseAJour = derniereMiseAJour;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "idUtilisateur=" + idUtilisateur +
                ", personnePhysique=" + personnePhysique +
                ", avs='" + avs + '\'' +
                ", idTitreCivil=" + idTitreCivil +
                ", nomUtilisateur='" + nomUtilisateur + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", idSexe=" + idSexe +
                ", idNationalite=" + idNationalite +
                ", idAdresse=" + idAdresse +
                ", email='" + email + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", motDePasse='" + motDePasse + '\'' +
                ", sel='" + sel + '\'' +
                ", creation=" + creation +
                ", derniereMiseAJour=" + derniereMiseAJour +
                '}';
    }
}
