package de.idos.updates;

import java.util.List;

public class VersionFinder {

    public static final NumericVersion BASE_VERSION = new NumericVersion(0, 0, 0);

    public Version findLatestVersion(List<Version> allVersions) {
        Version latestVersion = BASE_VERSION;
        for (Version version : allVersions) {
            if (version.isGreaterThan(latestVersion)) {
                latestVersion = version;
            }
        }
        return latestVersion;
    }
}