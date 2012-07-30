package de.idos.updates;

import de.idos.updates.store.OngoingInstallation;

public interface Updater {
  UpdateAvailability hasUpdate();

  Version getLatestVersion();

  OngoingInstallation updateToLatestVersion();

  Version getInstalledVersion();

  void runCheck();
}