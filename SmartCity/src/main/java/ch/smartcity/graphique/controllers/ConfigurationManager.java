package ch.smartcity.graphique.controllers;

import java.util.Locale;
import java.util.ResourceBundle;

public class ConfigurationManager {

    private final String messageBundlePropertiesFile =
            "ch/smartcity/graphique/messagesBundles/messageBundle";

    private ResourceBundle resourceBundle;

    private ConfigurationManager() {
        resourceBundle = ResourceBundle.getBundle(messageBundlePropertiesFile, Locale.getDefault());
    }

    public static ConfigurationManager getInstance() {
        return SingletonHolder.instance;
    }

    public ResourceBundle getResourceBundle() {
        return getInstance().resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        getInstance().resourceBundle = resourceBundle;
    }

    public String getString(String key) {
        return getResourceBundle().getString(key);
    }

    private static class SingletonHolder {
        private final static ConfigurationManager instance = new ConfigurationManager();
    }
}