package de.idos.updates;

import de.idos.updates.repository.Repository;

public class DefaultUpdateConnection implements UpdateConnection {

  private VersionTransfer transfer;
  private VersionDiscovery installedDiscovery;
  private VersionReceptacle receptacle;
  private VersionDiscovery availableDiscovery;

  public DefaultUpdateConnection(VersionStore versionStore, Repository versionRepository) {
    this(versionStore, versionStore, versionRepository, versionRepository);
  }

  public DefaultUpdateConnection(VersionDiscovery installedDiscovery, VersionReceptacle receptacle,
                                 VersionDiscovery availableDiscovery, VersionTransfer transfer) {
    this.installedDiscovery = installedDiscovery;
    this.receptacle = receptacle;
    this.availableDiscovery = availableDiscovery;
    this.transfer = transfer;
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
  public void install(Version latestVersion) {
    transfer.transferVersionTo(latestVersion, receptacle);
  }
}
