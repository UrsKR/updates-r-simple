package de.idos.updates;

import de.idos.updates.store.NullInstallation;
import de.idos.updates.store.OngoingInstallation;

public class NullVersionInstaller implements VersionInstaller {
  @Override
  public OngoingInstallation install(Version latestVersion) {
    return new NullInstallation();
  }
}