package de.idos.updates;

import de.idos.updates.store.OngoingInstallation;

public interface VersionInstaller {
  OngoingInstallation install(Version latestVersion);
}
