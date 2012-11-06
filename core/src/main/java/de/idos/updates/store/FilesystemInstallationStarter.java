package de.idos.updates.store;

import java.io.File;

public class FilesystemInstallationStarter implements InstallationStarter{
  @Override
  public Installation start(File targetFolder, ProgressReport report) {
    return FilesystemInstallation.create(targetFolder, report);
  }
}