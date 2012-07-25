package de.idos.updates.store;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static de.idos.updates.store.InstallationUtil.createMarkerFile;

public class FilesystemInstallation implements Installation {
    public static Installation create(File versionFolder, ProgressReport report) {
        try {
            versionFolder.mkdirs();
            boolean lockCreatedSuccessFully = createMarkerFile(versionFolder);
            if (!lockCreatedSuccessFully) {
                return createNullInstallation(report);
            }
            return new FilesystemInstallation(versionFolder);
        } catch (IOException e) {
            return createNullInstallation(report);
        }
    }

    private static Installation createNullInstallation(ProgressReport report) {
        report.updateAlreadyInProgress();
        return new NullInstallation();
    }

    private File versionFolder;

    private FilesystemInstallation(File versionFolder) throws IOException {
        this.versionFolder = versionFolder;
    }

    @Override
    public void addContent(DataInVersion dataInVersion) {
        dataInVersion.storeIn(versionFolder);
    }

    @Override
    public void abort() {
        FileUtils.deleteQuietly(versionFolder);
    }

    @Override
    public void finish() {
        InstallationUtil.deleteMarkerFile(versionFolder);
    }
}