package ch.smartcity.database.models;

import ch.smartcity.database.controllers.ConfigurationManager;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

@StaticMetamodel(Sexe.class)
public class Sexe_ {

    private static final ConfigurationManager CONFIGURATION_MANAGER =
            ConfigurationManager.getInstance();
    public static final String HOMME = CONFIGURATION_MANAGER.getString("sexe.homme");
    public static final String FEMME = CONFIGURATION_MANAGER.getString("sexe.femme");

    public static volatile SingularAttribute<Sexe, Integer> idSexe;
    public static volatile SingularAttribute<Sexe, String> nomSexe;
    public static volatile SingularAttribute<Sexe, Calendar> derniereMiseAJour;
    public static volatile SetAttribute<Sexe, Utilisateur> utilisateurSet;
}
