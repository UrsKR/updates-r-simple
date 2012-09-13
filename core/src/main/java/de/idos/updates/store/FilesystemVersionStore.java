package de.idos.updates.store;

import de.idos.updates.CleanupFailedException;
import de.idos.updates.Version;
import de.idos.updates.VersionStore;
import de.idos.updates.VersionedFile;
import de.idos.updates.VersionedFileFactory;
import de.idos.updates.VersionedFileFinder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FilesystemVersionStore implements VersionStore {

    private File folder;
    private ProgressReport report = new NullReport();
    private InstallationStarter installationStarter;

    public FilesystemVersionStore(File folder, InstallationStarter installationStarter) {
        this.folder = folder;
        folder.mkdirs();
        this.installationStarter = installationStarter;
    }

    @Override
    public Installation beginInstallation(Version version) {
        File targetFolder = getVersionFolder(version);
        return installationStarter.start(targetFolder, report);
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

    @Override
    public void reportAllProgressTo(ProgressReport report) {
        this.report = report;
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