package de.idos.updates.store;

import de.idos.updates.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
    public void addContent(Version version, File file) {
        InputStreamFactory factory = new FileStreamFactory(file);
        String fileName = file.getName();
        importStreamIntoVersion(version, fileName, factory);
    }

    @Override
    public void addContent(Version version, String fileName, URL urlToFile) {
        InputStreamFactory factory = new UrlStreamFactory(urlToFile);
        importStreamIntoVersion(version, fileName, factory);
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

    private void importStreamIntoVersion(Version version, String fileName, InputStreamFactory factory) {
        DataInVersion dataInVersion = new DataInVersion(factory);
        dataInVersion.storeIn(getVersionFolder(version), fileName);
    }

    private File getVersionFolder(Version version) {
        return new File(folder, version.asString());
    }
}