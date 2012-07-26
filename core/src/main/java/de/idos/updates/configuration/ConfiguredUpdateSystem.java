package de.idos.updates.configuration;

import de.idos.updates.*;

import java.io.File;
import java.util.Properties;

public class ConfiguredUpdateSystem {

    public static ConfiguredUpdateSystem loadProperties() {
        return new ConfiguredUpdateSystem();
    }

    private final VersionStore store;
    private VersionTransfer transfer;
    private VersionDiscovery discovery;

    private ConfiguredUpdateSystem() {
        Properties properties = new PropertiesLoader("update.properties").load();
        UpdateConfiguration configuration = new UpdateConfiguration(properties);
        Repository repository = createRepository(configuration);
        this.store = createVersionStore(configuration);
        this.discovery = repository;
        this.transfer = repository;
    }

    public ConfiguredUpdateSystem butUseDiscoveryMethod(VersionDiscovery discovery) {
        this.discovery = discovery;
        return this;
    }

    public UpdateSystem create() {
        return new DefaultUpdateSystem(store, discovery, transfer);
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