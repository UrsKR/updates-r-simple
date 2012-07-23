package de.idos.updates;

import de.idos.updates.store.ProgressReport;

import java.io.File;

public class UpdateSystem implements Updater {
    private final VersionStore versionStore;
    private final Repository repository;

    public UpdateSystem(VersionStore versionStore, Repository repository) {
        this.versionStore = versionStore;
        this.repository = repository;
    }

    public Updater checkForUpdates() {
        return new UpdateCheck(new UpdateConnection(versionStore, repository));
    }

    @Override
    public UpdateAvailability hasUpdate() {
        return checkForUpdates().hasUpdate();
    }

    @Override
    public Version getLatestVersion() {
        return checkForUpdates().getLatestVersion();
    }

    @Override
    public void updateToLatestVersion() {
        checkForUpdates().updateToLatestVersion();
    }

    public void removeOldVersions() {
        versionStore.removeOldVersions();
    }

    public void reportAllProgressTo(ProgressReport report) {
        repository.reportAllProgressTo(report);
    }

    public File getFolderForLatestVersion() {
        return versionStore.getFolderForLatestVersion();
    }
}