package de.idos.updates.integrationtest;

import de.idos.updates.Version;
import de.idos.updates.VersionStore;
import de.idos.updates.store.Installation;
import de.idos.updates.store.ProgressReport;

import java.io.File;
import java.util.concurrent.CountDownLatch;

public class SlowVersionStore implements VersionStore {
    private VersionStore versionStore;
    private CountDownLatch slowThreadStartSignal;

    public SlowVersionStore(VersionStore versionStore, CountDownLatch slowThreadStartSignal) {
        this.versionStore = versionStore;
        this.slowThreadStartSignal = slowThreadStartSignal;
    }

    @Override
    public Installation beginInstallation(Version version) {
        Installation installation = versionStore.beginInstallation(version);
        return new SlowInstallation(installation,slowThreadStartSignal);
    }

    @Override
    public void removeOldVersions() {
        versionStore.removeOldVersions();
    }

    @Override
    public Version getLatestVersion() {
        return versionStore.getLatestVersion();
    }

    @Override
    public File getFolderForVersionToRun() {
        return versionStore.getFolderForVersionToRun();
    }

    @Override
    public void reportAllProgressTo(ProgressReport report) {
        versionStore.reportAllProgressTo(report);
    }
}