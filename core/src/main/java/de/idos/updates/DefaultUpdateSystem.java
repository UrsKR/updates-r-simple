package de.idos.updates;

import de.idos.updates.store.ProgressReport;
import de.idos.updates.util.Announcer;

import java.io.File;

public class DefaultUpdateSystem implements UpdateSystem {
    private final VersionStore versionStore;
    private VersionDiscovery discovery;
    private VersionTransfer transfer;
    private final Announcer<ProgressReport> progressAnnouncer = Announcer.to(ProgressReport.class);

    public DefaultUpdateSystem(VersionStore versionStore, Repository repository) {
        this(versionStore, repository, repository);
    }

    public DefaultUpdateSystem(VersionStore versionStore, VersionDiscovery discovery, VersionTransfer transfer) {
        this.versionStore = versionStore;
        this.discovery = discovery;
        this.transfer = transfer;
        ProgressReport announcingReport = progressAnnouncer.announce();
        versionStore.reportAllProgressTo(announcingReport);
        discovery.reportAllProgressTo(announcingReport);
        transfer.reportAllProgressTo(announcingReport);
    }

    @Override
    public Updater checkForUpdates() {
        return new UpdateCheck(new UpdateConnection(versionStore, discovery, transfer));
    }

    @Override
    public void removeOldVersions() {
        versionStore.removeOldVersions();
    }

    @Override
    public void reportAllProgressTo(ProgressReport report) {
        progressAnnouncer.addListener(report);
    }

    @Override
    public File getFolderForVersionToRun() {
        return versionStore.getFolderForVersionToRun();
    }

    @Override
    public Version getInstalledVersion() {
        return versionStore.getLatestVersion();
    }

    @Override
    public void stopReportingTo(ProgressReport report) {
        progressAnnouncer.removeListener(report);
    }
}