package ch.smartcity.database.models;

import ch.smartcity.database.controllers.ConfigurationManager;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

@StaticMetamodel(Nationalite.class)
public class Nationalite_ {

    private static final ConfigurationManager CONFIGURATION_MANAGER =
            ConfigurationManager.getInstance();
    public static final String ALLEMANDE = CONFIGURATION_MANAGER
            .getString("nationalite.allemande");
    public static final String ESPAGNOLE = CONFIGURATION_MANAGER
            .getString("nationalite.espagnole");
    public static final String FRANCAISE = CONFIGURATION_MANAGER
            .getString("nationalite.francaise");
    public static final String ITELIENNE = CONFIGURATION_MANAGER
            .getString("nationalite.italienne");
    public static final String SUISSE = CONFIGURATION_MANAGER
            .getString("nationalite.suisse");

    public static volatile SingularAttribute<Nationalite, Integer> idNationalite;
    public static volatile SingularAttribute<Nationalite, String> nomNationalite;
    public static volatile SingularAttribute<Nationalite, Calendar> derniereMiseAJour;
    public static volatile SetAttribute<Nationalite, Utilisateur> utilisateurSet;
}
