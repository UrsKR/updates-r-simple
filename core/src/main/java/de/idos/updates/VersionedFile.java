package de.idos.updates;

import java.io.File;

public class VersionedFile {

    public Version version;
    public File file;

    public VersionedFile(Version version, File file) {
        this.version = version;
        this.file = file;
    }
}