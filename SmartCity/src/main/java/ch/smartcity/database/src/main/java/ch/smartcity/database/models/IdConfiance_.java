package ch.smartcity.database.models;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(IdConfiance.class)
public class IdConfiance_ {
    public static volatile SingularAttribute<IdConfiance, Utilisateur> utilisateur;
    public static volatile SingularAttribute<IdConfiance, RubriqueEnfant> rubriqueEnfant;
}
