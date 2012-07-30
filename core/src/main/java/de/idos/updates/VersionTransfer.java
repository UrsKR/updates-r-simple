package de.idos.updates;

import de.idos.updates.store.OngoingInstallation;
import de.idos.updates.store.ProgressReport;

public interface VersionTransfer {
  OngoingInstallation transferVersionTo(Version version, VersionReceptacle store);

  void reportAllProgressTo(ProgressReport report);
}