package de.idos.updates;

import de.idos.updates.store.ProgressReport;

import java.io.File;

public class DefaultUpdateSystem implements UpdateSystem {
    private final VersionStore versionStore;
    private final Repository repository;

    public DefaultUpdateSystem(VersionStore versionStore, Repository repository) {
        this.versionStore = versionStore;
        this.repository = repository;
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
        repository.reportAllProgressTo(report);
    }

    @Override
    public File getFolderForVersionToRun() {
        return versionStore.getFolderForVersionToRun();
    }
}