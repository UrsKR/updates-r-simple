package de.idos.updates.configuration;

import de.idos.updates.DefaultUpdateSystem;
import de.idos.updates.FilesystemRepository;
import de.idos.updates.HttpRepository;
import de.idos.updates.Repository;
import de.idos.updates.UpdateSystem;
import de.idos.updates.Version;
import de.idos.updates.VersionDiscovery;
import de.idos.updates.VersionStore;
import de.idos.updates.VersionStoreBuilder;
import de.idos.updates.VersionTransfer;

import java.io.File;
import java.util.Properties;

public class ConfiguredUpdateSystem {

  public static ConfiguredUpdateSystem loadProperties() {
    return new ConfiguredUpdateSystem();
  }

  private VersionDiscovery availableDiscovery;
  private VersionTransfer transfer;
  private final VersionStore store;
  private final UpdateConfiguration configuration;
  private Version installedVersionFallback;

  private ConfiguredUpdateSystem() {
    Properties properties = new PropertiesLoader("update.properties").load();
    this.configuration = new UpdateConfiguration(properties);
    Repository repository = createRepository();
    this.store = createVersionStore();
    this.availableDiscovery = repository;
    this.transfer = repository;
  }

  public ConfiguredUpdateSystem butDiscoverAvailableVersionThrough(VersionDiscovery discovery) {
    this.availableDiscovery = discovery;
    return this;
  }

  public ConfiguredUpdateSystem andIfTheInstalledVersionIsUnknownUse(Version version) {
    this.installedVersionFallback = version;
    return this;
  }

  public UpdateSystem create() {
    VersionDiscovery installedDiscovery = store;
    if (installedVersionFallback != null) {
      installedDiscovery = new FallbackVersionDiscovery(installedDiscovery, installedVersionFallback);
    }
    return new DefaultUpdateSystem(installedDiscovery, store, availableDiscovery, transfer);
  }

  private VersionStore createVersionStore() {
    VersionStore versionStore = VersionStoreBuilder.inUserHomeForApplication(configuration.getApplicationName()).create();
    if (configuration.getStrategy() == UpdateStrategy.LatestVersion) {
      return versionStore;
    } else {
      return new FixedVersionStore(new File(configuration.getLocationForFixedVersion()), versionStore);
    }
  }

  private Repository createRepository() {
    if (configuration.repositoryTypeForLatestVersion() == RepositoryType.File) {
      return new FilesystemRepository(new File(configuration.repositoryLocationForLatestVersion()));
    } else {
      return new HttpRepository(configuration.repositoryLocationForLatestVersion());
    }
  }
}