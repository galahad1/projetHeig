package database.controllers;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.LogManager;

public class ConfigurationManager {

    private static ResourceBundle resourceBundle;

    static {
        try {
            loadLoggingProperties("database/resources/logging.properties");
            setResourceBundle("database/resources/messagesBundles/messageBundle");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void initialize() {
    }

    public static ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public static void setResourceBundle(ResourceBundle resourceBundle) {
        ConfigurationManager.resourceBundle = resourceBundle;
    }

    public static void setResourceBundle(String baseNameResourceBundle) {
        setResourceBundle(ResourceBundle.getBundle(baseNameResourceBundle, Locale.getDefault()));
    }

    public static String getString(String key) {
        return resourceBundle.getString(key);
    }

    private static void loadLoggingProperties(String loggingPropertiesFile) throws IOException {
        LogManager.getLogManager().readConfiguration(ConfigurationManager.class.getClassLoader()
                .getResourceAsStream(loggingPropertiesFile));
    }
}
