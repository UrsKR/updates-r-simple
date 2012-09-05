package de.idos.updates;

import de.idos.updates.store.OngoingInstallation;

public interface InstallableUpdate extends Update {
  OngoingInstallation install();
}