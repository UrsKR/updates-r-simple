package de.idos.updates.configuration;

import de.idos.updates.*;

import java.io.File;
import java.util.Properties;

public class ConfiguredUpdateSystemFactory {

    public UpdateSystem create() {
        Properties properties = new PropertiesLoader().load("update.properties");
        UpdateConfiguration configuration = new UpdateConfiguration(properties);
        VersionStore store = createVersionStore(configuration);
        Repository repository = createRepository(configuration);
        return new UpdateSystem(store, repository);
    }

    private VersionStore createVersionStore(UpdateConfiguration configuration) {
        if (configuration.getStrategy() == UpdateStrategy.LatestVersion) {
            return VersionStoreBuilder.inUserHomeForApplication(configuration.getApplicationName()).create();
        } else {
            return new FixedVersionStore(new File(configuration.getLocationForFixedVersion()));
        }
    }

    private Repository createRepository(UpdateConfiguration configuration) {
        if (configuration.repositoryTypeForLatestVersion() == RepositoryType.File) {
            return new FilesystemRepository(new File(configuration.repositoryLocationForLatestVersion()));
        } else {
            return new HttpRepository(configuration.repositoryLocationForLatestVersion());
        }
    }
}