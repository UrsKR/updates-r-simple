package de.idos.updates;

public class UpdateConnection {

  private VersionTransfer transfer;
  private VersionDiscovery installedDiscovery;
  private VersionReceptacle receptacle;
  private VersionDiscovery availableDiscovery;

  public UpdateConnection(VersionStore versionStore, Repository versionRepository) {
    this(versionStore, versionStore, versionRepository, versionRepository);
  }

  public UpdateConnection(VersionDiscovery installedDiscovery, VersionReceptacle receptacle,
                          VersionDiscovery availableDiscovery, VersionTransfer transfer) {
    this.installedDiscovery = installedDiscovery;
    this.receptacle = receptacle;
    this.availableDiscovery = availableDiscovery;
    this.transfer = transfer;
  }

  public Version getLatestInstalledVersion() {
    return installedDiscovery.getLatestVersion();
  }

  public Version getLatestAvailableVersion() {
    return availableDiscovery.getLatestVersion();
  }

  public void install(Version latestVersion) {
    transfer.transferVersionTo(latestVersion, receptacle);
  }
}
