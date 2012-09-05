package de.idos.updates;

public class DefaultUpdateConnection implements UpdateConnection {

  private final VersionInstaller installer;
  private final VersionDiscovery installedDiscovery;
  private final VersionDiscovery availableDiscovery;

  public DefaultUpdateConnection(VersionDiscovery installedDiscovery,
                                 VersionDiscovery availableDiscovery, VersionInstaller versionInstaller) {
    this.installedDiscovery = installedDiscovery;
    this.availableDiscovery = availableDiscovery;
    this.installer = versionInstaller;
  }

  @Override
  public Version getLatestInstalledVersion() {
    return installedDiscovery.getLatestVersion();
  }

  @Override
  public InstallableUpdate getLatestAvailableUpdate() {
    Version availableVersion = availableDiscovery.getLatestVersion();
    return new DefaultUpdate(availableVersion, installer);
  }
}