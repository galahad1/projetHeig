package ch.smartcity.database.models;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

@StaticMetamodel(Utilisateur.class)
public class Utilisateur_ {

    public static volatile SingularAttribute<Utilisateur, Integer> idUtilisateur;
    public static volatile SingularAttribute<Utilisateur, Boolean> personnePhysique;
    public static volatile SingularAttribute<Utilisateur, String> avs;
    public static volatile SingularAttribute<Utilisateur, TitreCivil> titreCivil;
    public static volatile SingularAttribute<Utilisateur, String> nomUtilisateur;
    public static volatile SingularAttribute<Utilisateur, String> prenom;
    public static volatile SingularAttribute<Utilisateur, Calendar> dateDeNaissance;
    public static volatile SingularAttribute<Utilisateur, Sexe> sexe;
    public static volatile SingularAttribute<Utilisateur, Nationalite> nationalite;
    public static volatile SingularAttribute<Utilisateur, Adresse> adresse;
    public static volatile SingularAttribute<Utilisateur, String> email;
    public static volatile SingularAttribute<Utilisateur, String> pseudo;
    public static volatile SingularAttribute<Utilisateur, String> motDePasse;
    public static volatile SingularAttribute<Utilisateur, String> sel;
    public static volatile SingularAttribute<Utilisateur, Calendar> creation;
    public static volatile SingularAttribute<Utilisateur, Calendar> derniereMiseAJour;
    public static volatile SetAttribute<Utilisateur, Evenement> evenementSet;
    public static volatile SetAttribute<Utilisateur, Commentaire> commentaireSet;
    public static volatile SetAttribute<Utilisateur, Confiance> confianceSet;
}
