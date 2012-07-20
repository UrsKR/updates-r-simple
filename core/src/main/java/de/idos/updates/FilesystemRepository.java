package de.idos.updates;

import de.idos.updates.store.FileDataInVersion;

import java.io.File;
import java.util.List;

public class FilesystemRepository implements Repository {
    public static final String AVAILABLE_VERSIONS = "available_versions";
    private final File availableVersions;

    public FilesystemRepository(File root) {
        this.availableVersions = new File(root, AVAILABLE_VERSIONS);
        if (!availableVersions.exists()) {
            availableVersions.mkdirs();
        }
    }

    @Override
    public Version getLatestVersion() {
        List<VersionedFile> versionedFiles = new VersionedFileFactory().createVersionedFilesFrom(availableVersions);
        return new VersionedFileFinder().findLatestVersion(versionedFiles);
    }

    @Override
    public void transferVersionTo(Version version, VersionStore store) {
        store.addVersion(version);
        File versionFolder = new File(availableVersions, version.asString());
        for (File file : versionFolder.listFiles()) {
            store.addContent(version, new FileDataInVersion(file));
        }
    }
}