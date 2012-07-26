package de.idos.updates;

import de.idos.updates.store.Installation;
import de.idos.updates.store.ProgressReport;

import java.io.File;

public interface VersionReceptacle {
  Installation beginInstallation(Version version);

  void removeOldVersions();

  File getFolderForVersionToRun();

  void reportAllProgressTo(ProgressReport report);
}