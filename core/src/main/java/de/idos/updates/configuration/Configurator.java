package de.idos.updates.configuration;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static de.idos.updates.configuration.UpdateConfiguration.*;
import static de.idos.updates.configuration.UpdateStrategy.FixedVersion;
import static de.idos.updates.configuration.UpdateStrategy.LatestVersion;

public class Configurator {
    private Properties properties;

    public Configurator() {
        this.properties = new Properties();
    }

    public Configurator(Properties properties) {
        this.properties = properties;
    }

    public void saveConfiguration() throws IOException {
        File file = new File(".", "update.properties");
        saveConfigurationTo(file);
    }

    private void saveConfigurationTo(File file) throws IOException {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            properties.store(stream, "");
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    public void setApplicationNameTo(String name) {
        properties.put(UPDATE_APPLICATION_NAME, name);
    }

    public void toggleFixedVersion() {
        toggleStrategy(FixedVersion);
    }

    public void toggleLatestVersion() {
        toggleStrategy(LatestVersion);
    }

    private void toggleStrategy(UpdateStrategy newValue) {
        properties.put(UPDATE_STRATEGY, newValue.toString());
    }

    public void changeFixedVersionLocationTo(String location) {
        properties.put(UPDATE_FIXED_VERSION_LOCATION, location);
    }

    public void toggleHttpRepositoryForLatestVersion() {
        toggleRepositoryType(RepositoryType.HTTP);
    }

    public void toggleFileRepositoryForLatestVersion() {
        toggleRepositoryType(RepositoryType.File);
    }

    private void toggleRepositoryType(RepositoryType type) {
        properties.put(UPDATE_LATEST_VERSION_REPOSITORY_TYPE, type.toString());
    }

    public void setRepositoryLocationForLatestVersionTo(String location) {
        properties.put(UPDATE_LATEST_VERSION_REPOSITORY_LOCATION, location);
    }
}