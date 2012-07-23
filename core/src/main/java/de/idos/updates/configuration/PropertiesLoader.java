package de.idos.updates.configuration;

import java.io.*;
import java.util.Properties;

public class PropertiesLoader {
    public Properties load(String path) {
        try {
            Properties properties = new Properties();
            InputStream resource = getStreamWithProperties(path);
            properties.load(resource);
            resource.close();
            return properties;
        } catch (Exception e) {
            throw new ConfigurationFailedException(e);
        }
    }

    private InputStream getStreamWithProperties(String path) throws FileNotFoundException {
        File propertiesFile = new File(path);
        if (propertiesFile.exists()) {
            return new FileInputStream(propertiesFile);
        } else {
            return getClass().getClassLoader().getResourceAsStream(path);
        }
    }
}
