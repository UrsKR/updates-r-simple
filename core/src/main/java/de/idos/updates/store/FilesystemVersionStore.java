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
    }

    @Override
    public void addVersion(Version version) {
        File file = getVersionFolder(version);
        file.mkdirs();
    }

    @Override
    public void addContent(Version version, DataInVersion dataInVersion) {
        addDataToVersion(version, dataInVersion);
    }

    @Override
    public void removeOldVersions() {
        List<VersionedFile> versionedFiles = new VersionedFileFactory().createVersionedFilesFrom(folder);
        Version latestVersion = new VersionedFileFinder().findLatestVersion(versionedFiles);
        deleteAllButLatestVersion(versionedFiles, latestVersion);
    }

    @Override
    public void removeVersion(Version version) {
        try {
            File versionFolder = getVersionFolder(version);
            FileUtils.deleteDirectory(versionFolder);
        } catch (IOException e) {
            throw new CleanupFailedException("Could not delete old versions.", e);
        }
    }

    @Override
    public Version getLatestVersion() {
        List<VersionedFile> versionedFiles = new VersionedFileFactory().createVersionedFilesFrom(folder);
        return new VersionedFileFinder().findLatestVersion(versionedFiles);
    }

    public File getFolderForLatestVersion() {
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

    private void addDataToVersion(Version version, DataInVersion dataInVersion) {
        dataInVersion.storeIn(getVersionFolder(version));
    }

    private File getVersionFolder(Version version) {
        return new File(folder, version.asString());
    }
}