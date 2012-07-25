package de.idos.updates.lookup;

import de.idos.updates.Version;
import de.idos.updates.VersionedFile;
import de.idos.updates.VersionedFileFactory;
import de.idos.updates.VersionedFileFinder;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileLookup implements LookupStrategy {

    private File availableVersions;

    public FileLookup(File availableVersions) {
        this.availableVersions = availableVersions;
    }

    @Override
    public Version findLatestVersion() throws IOException {
        List<VersionedFile> versionedFiles = new VersionedFileFactory().createVersionedFilesFrom(availableVersions);
        return new VersionedFileFinder().findLatestVersion(versionedFiles);
    }
}