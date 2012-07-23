package de.idos.updates.configuration;

import java.util.Properties;

public class UpdateConfiguration {
    private Properties properties;

    public UpdateConfiguration(Properties properties) {
        this.properties = properties;
    }

    public String getApplicationName() {
        return properties.getProperty("update.applicationName");
    }

    public UpdateStrategy getStrategy() {
        return UpdateStrategy.valueOf(properties.getProperty("update.strategy"));
    }

    public String getLocationForFixedVersion() {
        return properties.getProperty("update.FixedVersion.location");
    }

    public RepositoryType repositoryTypeForLatestVersion() {
        return RepositoryType.valueOf(properties.getProperty("update.LatestVersion.repository.type"));
    }

    public String repositoryLocationForLatestVersion() {
        return properties.getProperty("update.LatestVersion.repository.location");
    }
}
