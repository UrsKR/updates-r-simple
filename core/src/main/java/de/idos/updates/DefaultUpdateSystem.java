package de.idos.updates;

import de.idos.updates.store.ProgressReport;
import de.idos.updates.util.Announcer;

import java.io.File;

public class DefaultUpdateSystem implements UpdateSystem {
    private final VersionStore versionStore;
    private final Repository repository;
    private final Announcer<ProgressReport> progressAnnouncer = Announcer.to(ProgressReport.class);

    public DefaultUpdateSystem(VersionStore versionStore, Repository repository) {
        this.versionStore = versionStore;
        this.repository = repository;
        ProgressReport announcingReport = progressAnnouncer.announce();
        versionStore.reportAllProgressTo(announcingReport);
        repository.reportAllProgressTo(announcingReport);
    }

    @Override
    public Updater checkForUpdates() {
        return new UpdateCheck(new UpdateConnection(versionStore, repository));
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