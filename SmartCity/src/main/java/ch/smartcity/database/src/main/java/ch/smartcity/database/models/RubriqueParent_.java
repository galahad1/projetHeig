package ch.smartcity.database.models;

import ch.smartcity.database.controllers.ConfigurationManager;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

/**
 * Modélise les attributs de la table rubriqueParent de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
@StaticMetamodel(RubriqueParent.class)
public class RubriqueParent_ {

    /**
     * Utilisé pour avoir des valeurs par défaut de nom de rubriques parents
     */
    private static final ConfigurationManager CONFIGURATION_MANAGER =
            ConfigurationManager.getInstance();
    public static final String TRAFIC = CONFIGURATION_MANAGER
            .getString("rubriqueParent.trafic");
    public static final String MANIFESTATIONS = CONFIGURATION_MANAGER
            .getString("rubriqueParent.manifestation");
    public static final String CHANTIERS = CONFIGURATION_MANAGER
            .getString("rubriqueParent.chantiers");
    public static final String DOLEANCES = CONFIGURATION_MANAGER
            .getString("rubriqueParent.doleances");

    public static volatile SingularAttribute<RubriqueParent, Integer> idRubriqueParent;
    public static volatile SingularAttribute<RubriqueParent, String> nomRubriqueParent;
    public static volatile SingularAttribute<RubriqueParent, Calendar> derniereMiseAJour;
    public static volatile SetAttribute<RubriqueParent, RubriqueEnfant> rubriqueEnfantSet;
}
