package ch.smartcity.database.models;

import ch.smartcity.database.controllers.ConfigurationManager;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

@StaticMetamodel(Statut.class)
public class Statut_ {

    private static final ConfigurationManager CONFIGURATION_MANAGER =
            ConfigurationManager.getInstance();
    public static final String EN_ATTENTE = CONFIGURATION_MANAGER.getString("statut.enAttente");
    public static final String TRAITE = CONFIGURATION_MANAGER.getString("statut.traite");
    public static final String REFUSE = CONFIGURATION_MANAGER.getString("statut.refuse");

    public static volatile SingularAttribute<Statut, Integer> idStatut;
    public static volatile SingularAttribute<Statut, String> nomStatut;
    public static volatile SingularAttribute<Statut, Calendar> derniereMiseAJour;
    public static volatile SetAttribute<Statut, Evenement> evenementSet;
}
