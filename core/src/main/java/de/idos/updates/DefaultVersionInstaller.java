package de.idos.updates;

import de.idos.updates.store.OngoingInstallation;

public class DefaultVersionInstaller implements VersionInstaller {
  private VersionTransfer transfer;
  private VersionReceptacle receptacle;

  public DefaultVersionInstaller(VersionTransfer transfer, VersionReceptacle receptacle) {
    this.transfer = transfer;
    this.receptacle = receptacle;
  }

  @Override
  public OngoingInstallation install(Version latestVersion) {
    return transfer.transferVersionTo(latestVersion, receptacle);
  }
}