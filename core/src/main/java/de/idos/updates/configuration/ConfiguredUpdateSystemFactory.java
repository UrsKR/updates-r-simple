package de.idos.updates.configuration;

import de.idos.updates.*;

import java.io.File;
import java.util.Properties;

public class ConfiguredUpdateSystemFactory {

    public UpdateSystem create() {
        Properties properties = new PropertiesLoader().load("update.properties");
        UpdateConfiguration configuration = new UpdateConfiguration(properties);
        String applicationName = configuration.getApplicationName();
        VersionStore store = VersionStoreBuilder.inUserHomeForApplication(applicationName).create();
        Repository repository = createRepository(configuration);
        return new UpdateSystem(store, repository);
    }

    private Repository createRepository(UpdateConfiguration configuration) {
        if (configuration.repositoryTypeForLatestVersion() == RepositoryType.File) {
            return new FilesystemRepository(new File(configuration.repositoryLocationForLatestVersion()));
        } else {
            return new HttpRepository(configuration.repositoryLocationForLatestVersion());
        }
    }
}