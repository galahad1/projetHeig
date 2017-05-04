package ch.smartcity.database.controllers;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.LogManager;

public class ConfigurationManager {

    private final String messageBundleFileProperties =
            "ch/smartcity/database/resources/messagesBundles/messageBundle";
    private final String loggingFileProperties =
            "ch/smartcity/database/resources/logging.properties";
    private final ResourceBundle resourceBundle;

    private ConfigurationManager() {
        resourceBundle = ResourceBundle.getBundle(messageBundleFileProperties, Locale.getDefault());

        try {
            LogManager.getLogManager().readConfiguration(getClass().getClassLoader()
                    .getResourceAsStream(loggingFileProperties));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConfigurationManager getInstance() {
        return SingletonHolder.instance;
    }

    public static void initialize() {
        getInstance();
    }

    public static ResourceBundle getResourceBundle() {
        return getInstance().resourceBundle;
    }

    public static String getString(String key) {
        return getResourceBundle().getString(key);
    }

    private static class SingletonHolder {
        private final static ConfigurationManager instance = new ConfigurationManager();
    }
}
