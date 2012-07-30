package de.idos.updates;

import de.idos.updates.store.OngoingInstallation;

public interface UpdateConnection {
  Version getLatestInstalledVersion();

  Version getLatestAvailableVersion();

  OngoingInstallation install(Version latestVersion);
}