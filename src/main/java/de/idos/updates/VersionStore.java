package de.idos.updates;

import java.io.File;

public interface VersionStore {
    void addVersion(Version version);

    void addContent(Version version, File file);
}
