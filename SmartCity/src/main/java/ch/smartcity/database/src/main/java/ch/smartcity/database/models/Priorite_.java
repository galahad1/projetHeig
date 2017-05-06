package ch.smartcity.database.models;

import ch.smartcity.database.controllers.ConfigurationManager;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

@StaticMetamodel(Priorite.class)
public class Priorite_ {

    private static final ConfigurationManager CONFIGURATION_MANAGER =
            ConfigurationManager.getInstance();
    public static final String MINEUR = CONFIGURATION_MANAGER
            .getString("priorite.mineur");
    public static final String GENANT = CONFIGURATION_MANAGER
            .getString("priorite.genant");
    public static final String PREOCUPANT = CONFIGURATION_MANAGER
            .getString("priorite.preocupant");
    public static final String IMPORTANT = CONFIGURATION_MANAGER
            .getString("priorite.important");
    public static final String URGENT = CONFIGURATION_MANAGER
            .getString("priorite.urgent");

    public static volatile SingularAttribute<Priorite, Integer> idPriorite;
    public static volatile SingularAttribute<Priorite, String> nomPriorite;
    public static volatile SingularAttribute<Priorite, Integer> niveau;
    public static volatile SingularAttribute<Priorite, Calendar> derniereMiseAJour;
    public static volatile SetAttribute<Priorite, Evenement> evenementSet;
}
