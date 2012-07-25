package de.idos.updates.store;

import de.idos.updates.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FilesystemVersionStore implements VersionStore {

    private File folder;

    public FilesystemVersionStore(File folder) {
        this.folder = folder;
        folder.mkdirs();
    }

    @Override
    public Installation beginInstallation(final Version version) {
        File file = getVersionFolder(version);
        file.mkdirs();
        return new FilesystemInstallation(file);
    }

    @Override
    public void removeOldVersions() {
        List<VersionedFile> versionedFiles = new VersionedFileFactory().createVersionedFilesFrom(folder);
        Version latestVersion = new VersionedFileFinder().findLatestVersion(versionedFiles);
        deleteAllButLatestVersion(versionedFiles, latestVersion);
    }

    @Override
    public Version getLatestVersion() {
        List<VersionedFile> versionedFiles = new VersionedFileFactory().createVersionedFilesFrom(folder);
        return new VersionedFileFinder().findLatestVersion(versionedFiles);
    }

    public File getFolderForVersionToRun() {
        return getVersionFolder(getLatestVersion());
    }

    private void deleteAllButLatestVersion(List<VersionedFile> versionedFiles, Version latestVersion) {
        try {
            for (VersionedFile versionedFile : versionedFiles) {
                if (versionedFile.version.isEqualTo(latestVersion)) {
                    continue;
                }
                FileUtils.deleteDirectory(versionedFile.file);
            }
        } catch (IOException e) {
            throw new CleanupFailedException("Could not delete old versions.", e);
        }
    }

    private File getVersionFolder(Version version) {
        return new File(folder, version.asString());
    }

}