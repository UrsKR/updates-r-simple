package de.idos.updates;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
            String fileName = file.getName();
            FileInputStream source = new FileInputStream(file);
            copyStreamToFileInVersion(version, fileName, source);
        } catch (IOException e) {
            throw new UpdateFailedException("Could not import version " + version.asString(), e);
        }
    }

    @Override
    public void addContent(Version version, String fileName, URL urlToFile) {
        try {
            InputStream source = urlToFile.openStream();
            copyStreamToFileInVersion(version, fileName, source);
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

    private void copyStreamToFileInVersion(Version version, String fileName, InputStream source) throws IOException {
        File targetFile = getTargetFile(version, fileName);
        FileUtils.copyInputStreamToFile(source, targetFile);
    }

    private File getTargetFile(Version version, String fileName) {
        File versionFolder = getVersionFolder(version);
        return new File(versionFolder, fileName);
    }

    private File getVersionFolder(Version version) {
        return new File(folder, version.asString());
    }
}
