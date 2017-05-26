package ch.smartcity.database.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

/**
 * Modélise un utilisateur au sein de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
public class Utilisateur implements Serializable {

    private Integer idUtilisateur;
    private Boolean personnePhysique;
    private String avs;
    private TitreCivil titreCivil;
    private String nomUtilisateur;
    private String prenom;
    private Calendar dateDeNaissance;
    private Sexe sexe;
    private Nationalite nationalite;
    private Adresse adresse;
    private String email;
    private String pseudo;
    private String motDePasse;
    private String sel;
    private Calendar creation;
    private Calendar derniereMiseAJour;
    private Set<Evenement> evenementSet;
    private Set<Commentaire> commentaireSet;
    private Set<Confiance> confianceSet;

    public Utilisateur() {
    }

    public Utilisateur(TitreCivil titreCivil,
                       String nomUtilisateur,
                       Adresse adresse,
                       String email,
                       String pseudo,
                       String motDePasse,
                       String sel) {
        this(false,
                null,
                titreCivil,
                nomUtilisateur,
                null,
                null,
                null,
                null,
                adresse,
                email,
                pseudo,
                motDePasse,
                sel);
    }

    public Utilisateur(String avs,
                       TitreCivil titreCivil,
                       String nomUtilisateur,
                       String prenom,
                       Calendar dateDeNaissance,
                       Sexe sexe,
                       Nationalite nationalite,
                       Adresse adresse,
                       String email,
                       String pseudo,
                       String motDePasse,
                       String sel) {
        this(true,
                avs,
                titreCivil,
                nomUtilisateur,
                prenom,
                dateDeNaissance,
                sexe,
                nationalite,
                adresse,
                email,
                pseudo,
                motDePasse,
                sel);
    }

    public Utilisateur(Boolean personnePhysique,
                       String avs,
                       TitreCivil titreCivil,
                       String nomUtilisateur,
                       String prenom,
                       Calendar dateDeNaissance,
                       Sexe sexe,
                       Nationalite nationalite,
                       Adresse adresse,
                       String email,
                       String pseudo,
                       String motDePasse,
                       String sel) {
        this.personnePhysique = personnePhysique;
        this.avs = avs;
        this.titreCivil = titreCivil;
        this.nomUtilisateur = nomUtilisateur.toLowerCase();
        this.prenom = prenom.toLowerCase();
        this.dateDeNaissance = dateDeNaissance;
        this.sexe = sexe;
        this.nationalite = nationalite;
        this.adresse = adresse;
        this.email = email.toLowerCase();
        this.pseudo = pseudo;
        this.motDePasse = motDePasse;
        this.sel = sel;
    }

    public Integer getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Integer idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public Boolean isPersonnePhysique() {
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

    public TitreCivil getTitreCivil() {
        return titreCivil;
    }

    public void setTitreCivil(TitreCivil titreCivil) {
        this.titreCivil = titreCivil;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur.toLowerCase();
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom.toLowerCase();
    }

    public Calendar getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(Calendar dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public Nationalite getNationalite() {
        return nationalite;
    }

    public void setNationalite(Nationalite nationalite) {
        this.nationalite = nationalite;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
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

    public Set<Evenement> getEvenementSet() {
        return evenementSet;
    }

    public void setEvenementSet(Set<Evenement> evenementSet) {
        this.evenementSet = evenementSet;
    }

    public Set<Commentaire> getCommentaireSet() {
        return commentaireSet;
    }

    public void setCommentaireSet(Set<Commentaire> commentaireSet) {
        this.commentaireSet = commentaireSet;
    }

    public Set<Confiance> getConfianceSet() {
        return confianceSet;
    }

    public void setConfianceSet(Set<Confiance> confianceSet) {
        this.confianceSet = confianceSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Utilisateur)) return false;

        Utilisateur that = (Utilisateur) o;

        return new EqualsBuilder()
                .append(idUtilisateur, that.idUtilisateur)
                .append(personnePhysique, that.personnePhysique)
                .append(avs, that.avs)
                .append(titreCivil, that.titreCivil)
                .append(nomUtilisateur, that.nomUtilisateur)
                .append(prenom, that.prenom)
                .append(dateDeNaissance, that.dateDeNaissance)
                .append(sexe, that.sexe)
                .append(nationalite, that.nationalite)
                .append(adresse, that.adresse)
                .append(email, that.email)
                .append(pseudo, that.pseudo)
                .append(motDePasse, that.motDePasse)
                .append(sel, that.sel)
                .append(creation, that.creation)
                .append(derniereMiseAJour, that.derniereMiseAJour)
                .append(evenementSet, that.evenementSet)
                .append(commentaireSet, that.commentaireSet)
                .append(confianceSet, that.confianceSet)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(idUtilisateur)
                .append(personnePhysique)
                .append(avs)
                .append(titreCivil)
                .append(nomUtilisateur)
                .append(prenom)
                .append(dateDeNaissance)
                .append(sexe)
                .append(nationalite)
                .append(adresse)
                .append(email)
                .append(pseudo)
                .append(motDePasse)
                .append(sel)
                .append(creation)
                .append(derniereMiseAJour)
                .append(evenementSet)
                .append(commentaireSet)
                .append(confianceSet)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "idUtilisateur=" + idUtilisateur +
                ", personnePhysique=" + personnePhysique +
                ", avs='" + avs + '\'' +
                ", titreCivil=" + titreCivil +
                ", nomUtilisateur='" + nomUtilisateur + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateDeNaissance=" + (dateDeNaissance != null ? dateDeNaissance.getTime() : "null") +
                ", sexe=" + sexe +
                ", nationalite=" + nationalite +
                ", adresse=" + adresse +
                ", email='" + email + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", motDePasse='" + motDePasse + '\'' +
                ", sel='" + sel + '\'' +
                ", creation=" + creation.getTime() +
                ", derniereMiseAJour=" + derniereMiseAJour.getTime() +
                '}';
    }
}
