package de.idos.updates;

import java.io.File;

public class FilesystemRepository implements Repository {
    private File root;

    public FilesystemRepository(File root) {
        this.root = root;
    }

    @Override
    public Version getLatestVersion() {
        return new NumericVersion(0, 0, 0);
    }
}
