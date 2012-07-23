package de.idos.updates.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    public Properties load(String path) {
        try {
            Properties properties = new Properties();
            InputStream result = getStreamFromClassPath(path);
            if (result != null) {
                fillPropertiesFromStream(properties, result);
            }
            File propertiesFile = new File(path);
            if (propertiesFile.exists()) {
                fillPropertiesFromStream(properties, new FileInputStream(propertiesFile));
            }
            if (properties.isEmpty()) {
                throw new ConfigurationFailedException("Could not find configuration file " + path + " on either classpath or filesystem.");
            }
            return properties;
        } catch (Exception e) {
            throw new ConfigurationFailedException(e);
        }
    }

    private void fillPropertiesFromStream(Properties properties, InputStream result) throws IOException {
        properties.load(result);
        result.close();
    }

    private InputStream getStreamFromClassPath(String path) {
        InputStream result;
        result = getClass().getClassLoader().getResourceAsStream(path);
        return result;
    }

}
