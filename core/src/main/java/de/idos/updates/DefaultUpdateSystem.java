package de.idos.updates;

import de.idos.updates.store.ProgressReport;
import de.idos.updates.util.Announcer;

import java.io.File;

public class DefaultUpdateSystem implements UpdateSystem {
  private VersionDiscovery availableDiscovery;
  private VersionTransfer transfer;
  private final Announcer<ProgressReport> progressAnnouncer = Announcer.to(ProgressReport.class);
  private VersionReceptacle receptacle;
  private VersionDiscovery installedDiscovery;

  public DefaultUpdateSystem(VersionStore versionStore, Repository repository) {
    this(versionStore, versionStore, repository, repository);
  }

  public DefaultUpdateSystem(VersionDiscovery installedDiscovery, VersionReceptacle receptacle,
                             VersionDiscovery availableDiscovery, VersionTransfer transfer) {
    this.availableDiscovery = availableDiscovery;
    this.transfer = transfer;
    this.installedDiscovery = installedDiscovery;
    this.receptacle = receptacle;
    ProgressReport announcingReport = progressAnnouncer.announce();
    installedDiscovery.reportAllProgressTo(announcingReport);
    availableDiscovery.reportAllProgressTo(announcingReport);
    transfer.reportAllProgressTo(announcingReport);
    receptacle.reportAllProgressTo(announcingReport);
  }

  @Override
  public Updater checkForUpdates() {
    return new UpdateCheck(new DefaultUpdateConnection(installedDiscovery, receptacle, availableDiscovery, transfer));
  }

  @Override
  public void removeOldVersions() {
    receptacle.removeOldVersions();
  }

  @Override
  public void reportAllProgressTo(ProgressReport report) {
    progressAnnouncer.addListener(report);
  }

  @Override
  public File getFolderForVersionToRun() {
    return receptacle.getFolderForVersionToRun();
  }

  @Override
  public Version getInstalledVersion() {
    return installedDiscovery.getLatestVersion();
  }

  @Override
  public void stopReportingTo(ProgressReport report) {
    progressAnnouncer.removeListener(report);
  }
}