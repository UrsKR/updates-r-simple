package de.idos.updates.configuration;

import de.idos.updates.*;

import java.io.File;
import java.util.Properties;

public class ConfiguredUpdateSystemFactory {

    public UpdateSystem create() {
        Properties properties = new PropertiesLoader("update.properties").load();
        UpdateConfiguration configuration = new UpdateConfiguration(properties);
        VersionStore store = createVersionStore(configuration);
        Repository repository = createRepository(configuration);
        return new DefaultUpdateSystem(store, repository);
    }

    private VersionStore createVersionStore(UpdateConfiguration configuration) {
        VersionStore versionStore = VersionStoreBuilder.inUserHomeForApplication(configuration.getApplicationName()).create();
        if (configuration.getStrategy() == UpdateStrategy.LatestVersion) {
            return versionStore;
        } else {
            return new FixedVersionStore(new File(configuration.getLocationForFixedVersion()), versionStore);
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