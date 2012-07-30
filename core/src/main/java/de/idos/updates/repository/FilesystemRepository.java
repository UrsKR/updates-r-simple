package de.idos.updates.repository;

import de.idos.updates.install.FileInstaller;
import de.idos.updates.install.InstallationStrategy;
import de.idos.updates.lookup.FileLookup;
import de.idos.updates.lookup.LookupStrategy;
import de.idos.updates.store.Installation;

import java.io.File;

public class FilesystemRepository extends AbstractRepository<File> {
  public static final String AVAILABLE_VERSIONS = "available_versions";
  private final File availableVersions;

  public FilesystemRepository(File root) {
    this.availableVersions = new File(root, AVAILABLE_VERSIONS);
    if (!availableVersions.exists()) {
      availableVersions.mkdirs();
    }
  }

  @Override
  protected LookupStrategy createLookup() {
    return new FileLookup(availableVersions);
  }

  @Override
  protected InstallationStrategy<File> createInstallationStrategy(Installation installation) {
    return new FileInstaller(getReport(), availableVersions, installation);
  }
}