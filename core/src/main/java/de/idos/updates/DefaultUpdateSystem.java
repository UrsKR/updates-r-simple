package de.idos.updates;

import de.idos.updates.store.ProgressReport;
import de.idos.updates.util.Announcer;

import java.io.File;

public class DefaultUpdateSystem implements UpdateSystem {
  private final Announcer<ProgressReport> progressAnnouncer = Announcer.to(ProgressReport.class);
  private final VersionDiscovery availableDiscovery;
  private final VersionDiscovery installedDiscovery;
  private final VersionTransfer transfer;
  private final VersionReceptacle receptacle;

  public DefaultUpdateSystem(VersionDiscovery installedDiscovery, VersionReceptacle receptacle,
                             VersionDiscovery availableDiscovery, VersionTransfer transfer) {
    this.availableDiscovery = availableDiscovery;
    this.installedDiscovery = installedDiscovery;
    this.transfer = transfer;
    this.receptacle = receptacle;
    ProgressReport announcingReport = progressAnnouncer.announce();
    installedDiscovery.reportAllProgressTo(announcingReport);
    availableDiscovery.reportAllProgressTo(announcingReport);
    transfer.reportAllProgressTo(announcingReport);
    receptacle.reportAllProgressTo(announcingReport);
  }

  @Override
  public Updater checkForUpdates() {
    return new UpdateCheck(new DefaultUpdateConnection(installedDiscovery, availableDiscovery, new DefaultVersionInstaller(transfer, receptacle)));
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