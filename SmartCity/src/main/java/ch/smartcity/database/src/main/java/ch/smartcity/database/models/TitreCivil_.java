package ch.smartcity.database.models;

import ch.smartcity.database.controllers.ConfigurationManager;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

@StaticMetamodel(TitreCivil.class)
public class TitreCivil_ {

    private static final ConfigurationManager CONFIGURATION_MANAGER =
            ConfigurationManager.getInstance();
    public static final String MONSIEUR = CONFIGURATION_MANAGER
            .getString("titreCivil.monsieur");
    public static final String MADAME = CONFIGURATION_MANAGER
            .getString("titreCivil.madame");
    public static final String MADEMOISELLE = CONFIGURATION_MANAGER
            .getString("titreCivil.mademoiselle");
    public static final String ENTREPRISE = CONFIGURATION_MANAGER
            .getString("titreCivil.entreprise");

    public static volatile SingularAttribute<TitreCivil, Integer> idTitreCivil;
    public static volatile SingularAttribute<TitreCivil, String> titre;
    public static volatile SingularAttribute<TitreCivil, String> abreviation;
    public static volatile SingularAttribute<TitreCivil, Calendar> derniereMiseAJour;
    public static volatile SetAttribute<TitreCivil, Utilisateur> utilisateurSet;
}
