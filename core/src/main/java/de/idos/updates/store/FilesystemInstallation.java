package de.idos.updates.store;

import java.io.File;

public class FilesystemInstallation implements Installation {
    private File versionFolder;

    public FilesystemInstallation(File versionFolder) {
        this.versionFolder = versionFolder;
    }

    @Override
    public void addContent(DataInVersion dataInVersion) {
        dataInVersion.storeIn(versionFolder);
    }

    @Override
    public void abort() {
        versionFolder.delete();
    }
}