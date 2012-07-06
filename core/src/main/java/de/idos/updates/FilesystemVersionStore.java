package de.idos.updates;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class FilesystemVersionStore implements VersionStore {

    public static FilesystemVersionStore inUserHomeForApplication(String applicationName) {
        File userHome = new File(System.getProperty("user.home"));
        File applicationHome = new File(userHome, "." + applicationName);
        File versionStore = new File(applicationHome, "versions");
        return new FilesystemVersionStore(versionStore);
    }

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
        try {
            File versionFolder = getVersionFolder(version);
            FileUtils.copyFileToDirectory(file, versionFolder);
        } catch (IOException e) {
            throw new UpdateFailedException("Could not import version " + version.asString(), e);
        }
    }

    @Override
    public void addContent(Version version, String fileName, URL fileUrl) {
        try {
            File versionFolder = getVersionFolder(version);
            FileUtils.copyURLToFile(fileUrl, new File(versionFolder, fileName));
        } catch (IOException e) {
            throw new UpdateFailedException("Could not import version " + version.asString(), e);
        }
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

    private File getVersionFolder(Version version) {
        return new File(folder, version.asString());
    }
}
