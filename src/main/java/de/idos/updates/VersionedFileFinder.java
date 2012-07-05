package de.idos.updates;

import java.util.List;

public class VersionedFileFinder {
    public Version findLatestVersion(List<VersionedFile> versionedFiles) {
        Version latestVersion = new NumericVersion(0, 0, 0);
        for (VersionedFile versionedFile : versionedFiles) {
            Version version = versionedFile.version;
            if (version.isGreaterThan(latestVersion)) {
                latestVersion = version;
            }
        }
        return latestVersion;
    }
}