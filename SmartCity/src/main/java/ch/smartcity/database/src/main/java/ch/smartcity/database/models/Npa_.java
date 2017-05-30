package ch.smartcity.database.models;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

/**
 * Modélise les attributs de la table npa de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
@StaticMetamodel(Npa.class)
public class Npa_ {

    public static volatile SingularAttribute<Npa, Integer> idNpa;
    public static volatile SingularAttribute<Npa, String> numeroNpa;
    public static volatile SingularAttribute<Npa, Calendar> derniereMiseAJour;
    public static volatile SetAttribute<Npa, Adresse> adressesSet;
}
