package de.idos.updates.repository;

import de.idos.updates.Version;
import de.idos.updates.VersionReceptacle;
import de.idos.updates.install.DefaultInstaller;
import de.idos.updates.install.InstallationStrategy;
import de.idos.updates.lookup.LookupStrategy;
import de.idos.updates.lookup.VersionLookup;
import de.idos.updates.store.Installation;
import de.idos.updates.store.NullReport;
import de.idos.updates.store.ProgressReport;

public abstract class AbstractRepository<T> implements Repository {

  private ProgressReport report = new NullReport();

  @Override
  public Version getLatestVersion() {
    return new VersionLookup(createLookup(), report).lookUpLatestVersion();
  }

  @Override
  public void transferVersionTo(Version version, VersionReceptacle store) {
    Installation installation = store.beginInstallation(version);
    InstallationStrategy<T> strategy = createInstallationStrategy(installation);
    new DefaultInstaller<T>(strategy, report).install(version);
  }

  @Override
  public void reportAllProgressTo(ProgressReport report) {
    this.report = report;
  }

  protected ProgressReport getReport() {
    return report;
  }

  protected abstract InstallationStrategy<T> createInstallationStrategy(Installation installation);

  protected abstract LookupStrategy createLookup();
}