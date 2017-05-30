package ch.smartcity.database.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.LogManager;

/**
 * Gère le chargement et l'accès à des propriétés contenues dans des fichiers
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
public class ConfigurationManager {

    /**
     * Nom de base des fichiers utilisés par le resourceBundle
     */
    private final String messageBundlePropertiesFile =
            "ch/smartcity/database/resources/messagesBundles/messageBundle";

    /**
     * Fichier de propriétés utilisé pour définir les paramètres du système de log
     */
    private final String loggingPropertiesFile =
            "ch/smartcity/database/resources/logging.properties";

    /**
     * Dossier de destination pour les fichiers de log
     */
    private final String loggingFileDestination =
            System.getProperty("user.home") + File.separator
                    + "SmartCity" + File.separator
                    + "Logs" + File.separator;

    /**
     * Utilisé pour obtenir des messages dans la langue correspondante au système courant
     */
    private ResourceBundle resourceBundle;

    private ConfigurationManager() {
        resourceBundle = ResourceBundle.getBundle(messageBundlePropertiesFile, Locale.getDefault());

        // Création de l'arborescence de dossier pour contenir les logs
        File file = new File(loggingFileDestination);
        file.mkdirs();

        try {
            // Définit les propriétés du logger
            LogManager.getLogManager().readConfiguration(getClass().getClassLoader()
                    .getResourceAsStream(loggingPropertiesFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fournit l'unique instance de la classe (singleton)
     *
     * @return unique instance de la classe
     */
    public static ConfigurationManager getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Fournit le resourceBundle gérant l'affichage de messages dans la langue correspondante
     * au système courant
     *
     * @return resourceBundle gérant l'affichage de messages dans la langue correspondante
     * au système courant
     */
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
     * Définit le resourceBundle gérant l'affichage de messages dans la langue correspondante
     * au système courant
     *
     * @param resourceBundle resourceBundle gérant l'affichage de messages dans la langue
     *                       correspondante au système courant
     */
    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    /**
     * Fournit la chaîne de caractères correspondant à la clé de la propriété contenue dans le
     * fichier du resourceBundle
     *
     * @param key clé de la propriété contenue dans le fichier du resourceBundle
     * @return chaîne de caractères correspondant à la clé de la propriété contenue dans le
     * fichier du resourceBundle
     */
    public String getString(String key) {

        // Récupère la chaîne de caractères et l'encode au format UTF-8
        return new String(resourceBundle.getString(key).getBytes(
                StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8);
    }

    /**
     * Utilisé pour créer un singleton de la classe
     */
    private static class SingletonHolder {
        private final static ConfigurationManager instance = new ConfigurationManager();
    }
}
