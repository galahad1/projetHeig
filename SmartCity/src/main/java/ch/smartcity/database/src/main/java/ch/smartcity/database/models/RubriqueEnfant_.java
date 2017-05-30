package ch.smartcity.database.models;

import ch.smartcity.database.controllers.ConfigurationManager;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

/**
 * Modélise les attributs de la table rubriqueEnfant de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
@StaticMetamodel(RubriqueEnfant.class)
public class RubriqueEnfant_ {

    /**
     * Utilisé pour avoir des valeurs par défaut de nom de rubriques enfants
     */
    private static final ConfigurationManager CONFIGURATION_MANAGER =
            ConfigurationManager.getInstance();
    public static final String ACCIDENTS = CONFIGURATION_MANAGER
            .getString("rubriqueEnfant.accidents");
    public static final String TRAVAUX = CONFIGURATION_MANAGER
            .getString("rubriqueEnfant.travaux");
    public static final String MANIFESTATIONS = CONFIGURATION_MANAGER
            .getString("rubriqueEnfant.manifestations");
    public static final String RENOVATIONS = CONFIGURATION_MANAGER
            .getString("rubriqueEnfant.renovations");
    public static final String CONSTRUCTIONS = CONFIGURATION_MANAGER
            .getString("rubriqueEnfant.constructions");

    public static volatile SingularAttribute<RubriqueEnfant, Integer> idRubriqueEnfant;
    public static volatile SingularAttribute<RubriqueEnfant, RubriqueParent> rubriqueParent;
    public static volatile SingularAttribute<RubriqueEnfant, String> nomRubriqueEnfant;
    public static volatile SingularAttribute<RubriqueEnfant, Calendar> derniereMiseAJour;
    public static volatile SetAttribute<RubriqueEnfant, Evenement> evenementSet;
    public static volatile SetAttribute<RubriqueEnfant, Confiance> confianceSet;
}
