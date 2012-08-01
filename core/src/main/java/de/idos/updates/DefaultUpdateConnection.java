package de.idos.updates;

import de.idos.updates.repository.Repository;

public class DefaultUpdateConnection implements UpdateConnection {

  private final VersionInstaller installer;
  private final VersionDiscovery installedDiscovery;
  private final VersionDiscovery availableDiscovery;

  public DefaultUpdateConnection(VersionStore versionStore, Repository versionRepository) {
    this(versionStore, versionStore, versionRepository, versionRepository);
  }

  public DefaultUpdateConnection(VersionDiscovery installedDiscovery, VersionReceptacle receptacle,
                                 VersionDiscovery availableDiscovery, VersionTransfer transfer) {
    this.installedDiscovery = installedDiscovery;
    this.availableDiscovery = availableDiscovery;
    this.installer = new DefaultVersionInstaller(transfer, receptacle);
  }

  @Override
  public Version getLatestInstalledVersion() {
    return installedDiscovery.getLatestVersion();
  }

  @Override
  public Version getLatestAvailableVersion() {
    return availableDiscovery.getLatestVersion();
  }

  @Override
  public Update getLatestAvailableUpdate() {
    return new DefaultUpdate(getLatestAvailableVersion(), installer);
  }
}