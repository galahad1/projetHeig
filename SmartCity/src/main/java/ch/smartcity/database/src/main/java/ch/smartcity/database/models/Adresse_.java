package ch.smartcity.database.models;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

/**
 * Modélise les attributs de la table adresse de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
@StaticMetamodel(Adresse.class)
public class Adresse_ {

    public static volatile SingularAttribute<Adresse, Integer> idAdresse;
    public static volatile SingularAttribute<Adresse, Rue> rue;
    public static volatile SingularAttribute<Adresse, String> numeroDeRue;
    public static volatile SingularAttribute<Adresse, Npa> npa;
    public static volatile SingularAttribute<Adresse, Calendar> derniereMiseAJour;
    public static volatile SetAttribute<Adresse, Utilisateur> utilisateurSet;
    public static volatile SetAttribute<Adresse, Evenement> evenementSet;
}
