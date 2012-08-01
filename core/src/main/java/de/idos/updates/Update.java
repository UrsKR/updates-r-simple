package de.idos.updates;

import de.idos.updates.store.OngoingInstallation;

public interface Update {
  OngoingInstallation install();

  UpdateAvailability isUpdateFrom(Version currentVersion);

  Version getVersion();
}