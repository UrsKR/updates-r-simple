package de.idos.updates.configuration;

import java.util.Properties;

public class UpdateConfiguration {
    public static final String UPDATE_APPLICATION_NAME = "update.applicationName";
    public static final String UPDATE_STRATEGY = "update.strategy";
    public static final String UPDATE_LATEST_VERSION_REPOSITORY_TYPE = "update.LatestVersion.repository.type";
    public static final String UPDATE_FIXED_VERSION_LOCATION = "update.FixedVersion.location";
    public static final String UPDATE_LATEST_VERSION_REPOSITORY_LOCATION = "update.LatestVersion.repository.location";

    private Properties properties;

    public UpdateConfiguration(Properties properties) {
        this.properties = properties;
    }

    public String getApplicationName() {
        return properties.getProperty(UPDATE_APPLICATION_NAME);
    }

    public UpdateStrategy getStrategy() {
        return UpdateStrategy.valueOf(properties.getProperty(UPDATE_STRATEGY));
    }

    public String getLocationForFixedVersion() {
        return properties.getProperty(UPDATE_FIXED_VERSION_LOCATION);
    }

    public RepositoryType repositoryTypeForLatestVersion() {
        return RepositoryType.valueOf(properties.getProperty(UPDATE_LATEST_VERSION_REPOSITORY_TYPE));
    }

    public String repositoryLocationForLatestVersion() {
        return properties.getProperty(UPDATE_LATEST_VERSION_REPOSITORY_LOCATION);
    }
}
