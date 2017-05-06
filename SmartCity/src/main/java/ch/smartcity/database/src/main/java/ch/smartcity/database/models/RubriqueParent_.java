package ch.smartcity.database.models;

import ch.smartcity.database.controllers.ConfigurationManager;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

@StaticMetamodel(RubriqueParent.class)
public class RubriqueParent_ {

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
