package de.idos.updates.integrationtest;

import de.idos.updates.Version;
import de.idos.updates.VersionStore;
import de.idos.updates.store.Installation;

import java.io.File;

public class SlowVersionStore implements VersionStore {
    private VersionStore versionStore;

    public SlowVersionStore(VersionStore versionStore) {
        this.versionStore = versionStore;
    }

    @Override
    public Installation beginInstallation(Version version) {
        Installation installation = versionStore.beginInstallation(version);
        waitAMoment();
        return installation;
    }

    @Override
    public void removeOldVersions() {
        versionStore.removeOldVersions();
        waitAMoment();
    }

    @Override
    public Version getLatestVersion() {
        waitAMoment();
        return versionStore.getLatestVersion();
    }

    @Override
    public File getFolderForVersionToRun() {
        waitAMoment();
        return versionStore.getFolderForVersionToRun();
    }

    private void waitAMoment() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
