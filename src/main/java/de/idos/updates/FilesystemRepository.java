package de.idos.updates;

import java.io.File;

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
        String[] fileList = availableVersions.list();
        Version latestVersion = new NumericVersion(0, 0, 0);
        for (String versionAsString : fileList) {
            String[] versionParts = versionAsString.split("\\.");
            NumericVersion version = new NumericVersion(Integer.valueOf(versionParts[0]), Integer.valueOf(versionParts[1]), Integer.valueOf(versionParts[2]));
            if (version.isGreaterThan(latestVersion)) {
                latestVersion = version;
            }
        }
        return latestVersion;
    }

    @Override
    public void transferVersionTo(Version version, VersionStore store) {
        store.addVersion(version);
        File versionFolder = new File(availableVersions, version.asString());
        for (File file : versionFolder.listFiles()) {
            store.addContent(version, file);
        }
    }
}