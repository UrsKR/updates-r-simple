package de.idos.updates;

import java.io.File;

public class FilesystemRepository implements Repository {
    public static final String AVAILABLE_VERSIONS = "available_versions";
    private File root;

    public FilesystemRepository(File root) {
        this.root = root;
    }

    @Override
    public Version getLatestVersion() {
        File file = new File(root, AVAILABLE_VERSIONS);
        String[] fileList = file.list();
        String[] versionParts = fileList[0].split("\\.");
        return new NumericVersion(Integer.valueOf(versionParts[0]), Integer.valueOf(versionParts[1]), Integer.valueOf(versionParts[2]));
    }
}
