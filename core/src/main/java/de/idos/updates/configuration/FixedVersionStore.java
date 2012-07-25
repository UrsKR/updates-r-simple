package de.idos.updates.configuration;

import de.idos.updates.Version;
import de.idos.updates.VersionStore;
import de.idos.updates.store.Installation;

import java.io.File;

public class FixedVersionStore implements VersionStore {
    private File rootFolder;
    private VersionStore wrapped;

    public FixedVersionStore(File rootFolder, VersionStore wrapped) {
        this.rootFolder = rootFolder;
        this.wrapped = wrapped;
    }

    @Override
    public Installation beginInstallation(Version version) {
        return wrapped.beginInstallation(version);
    }

    @Override
    public void removeOldVersions() {
        wrapped.removeOldVersions();
    }

    @Override
    public Version getLatestVersion() {
        return wrapped.getLatestVersion();
    }

    @Override
    public File getFolderForVersionToRun() {
        return rootFolder;
    }
}