package de.idos.updates.configuration;

import java.io.IOException;
import java.util.Properties;

import static de.idos.updates.configuration.UpdateConfiguration.UPDATE_APPLICATION_NAME;
import static de.idos.updates.configuration.UpdateConfiguration.UPDATE_FIXED_VERSION_LOCATION;
import static de.idos.updates.configuration.UpdateConfiguration.UPDATE_LATEST_VERSION_REPOSITORY_LOCATION;
import static de.idos.updates.configuration.UpdateConfiguration.UPDATE_LATEST_VERSION_REPOSITORY_TYPE;
import static de.idos.updates.configuration.UpdateConfiguration.UPDATE_STRATEGY;
import static de.idos.updates.configuration.UpdateStrategy.FixedVersion;
import static de.idos.updates.configuration.UpdateStrategy.LatestVersion;

public class Configurator {
  private final PropertiesSaver saver = new PropertiesSaver("update.properties");
    private Properties properties;

    public Configurator() {
        this.properties = new Properties();
    }

    public Configurator(Properties properties) {
        this.properties = properties;
    }

    public void saveConfiguration() throws IOException {
      saver.save(properties);
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