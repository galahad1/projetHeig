package ch.smartcity.database.models;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

/**
 * Modélise les attributs de la table confiance de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
@StaticMetamodel(Confiance.class)
public class Confiance_ {

    public static volatile SingularAttribute<Confiance, IdConfiance> idConfiance;
    public static volatile SingularAttribute<Confiance, Calendar> creation;
}
