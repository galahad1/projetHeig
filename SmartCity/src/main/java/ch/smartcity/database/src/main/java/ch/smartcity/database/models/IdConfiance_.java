package ch.smartcity.database.models;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Modélise les attributs de l'identifiant de la table confiance de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
@StaticMetamodel(IdConfiance.class)
public class IdConfiance_ {

    public static volatile SingularAttribute<IdConfiance, Utilisateur> utilisateur;
    public static volatile SingularAttribute<IdConfiance, RubriqueEnfant> rubriqueEnfant;
}
