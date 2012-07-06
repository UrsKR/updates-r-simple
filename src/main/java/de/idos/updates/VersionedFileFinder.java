package de.idos.updates;

import java.util.ArrayList;
import java.util.List;

public class VersionedFileFinder {
    public Version findLatestVersion(List<VersionedFile> versionedFiles) {
        List<Version> allVersions = new ArrayList<Version>();
        for (VersionedFile versionedFile : versionedFiles) {
            allVersions.add(versionedFile.version);
        }
        return new VersionFinder().findLatestVersion(allVersions);
    }
}