package de.idos.updates;

import de.idos.updates.repository.Repository;

public class DefaultUpdateConnection implements UpdateConnection {

  private final VersionInstaller installer;
  private final VersionDiscovery installedDiscovery;
  private final VersionDiscovery availableDiscovery;

  public DefaultUpdateConnection(VersionStore versionStore, Repository versionRepository) {
    this(versionStore, versionRepository, new DefaultVersionInstaller(versionRepository, versionStore));
  }

  public DefaultUpdateConnection(VersionDiscovery installedDiscovery,
                                 VersionDiscovery availableDiscovery, DefaultVersionInstaller versionInstaller) {
    this.installedDiscovery = installedDiscovery;
    this.availableDiscovery = availableDiscovery;
    this.installer = versionInstaller;
  }

  @Override
  public Version getLatestInstalledVersion() {
    return installedDiscovery.getLatestVersion();
  }

  @Override
  public Update getLatestAvailableUpdate() {
    Version availableVersion = availableDiscovery.getLatestVersion();
    return new DefaultUpdate(availableVersion, installer);
  }
}