package de.idos.updates.repository;

import de.idos.updates.Version;
import de.idos.updates.VersionReceptacle;
import de.idos.updates.install.InstallationStrategy;
import de.idos.updates.install.ThreadedInstaller;
import de.idos.updates.lookup.LookupStrategy;
import de.idos.updates.lookup.VersionLookup;
import de.idos.updates.store.Installation;
import de.idos.updates.store.NullReport;
import de.idos.updates.store.OngoingInstallation;
import de.idos.updates.store.ProgressReport;

public abstract class AbstractRepository<T> implements Repository {

  private ProgressReport report = new NullReport();

  @Override
  public Version getLatestVersion() {
    return new VersionLookup(createLookup(), report).lookUpLatestVersion();
  }

  @Override
  public OngoingInstallation transferVersionTo(Version version, VersionReceptacle store) {
    Installation installation = store.beginInstallation(version);
    InstallationStrategy<T> strategy = createInstallationStrategy(installation);
    ThreadedInstaller.PrepareInstallation(strategy, report).install(version);
    return installation;
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