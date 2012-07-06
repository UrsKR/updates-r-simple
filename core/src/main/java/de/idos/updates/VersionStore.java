package de.idos.updates;

import java.io.File;
import java.net.URL;

public interface VersionStore {
    void addVersion(Version version);

    void addContent(Version version, File file);

    void addContent(Version version, String fileName, URL fileUrl);

    void removeOldVersions();

    void removeVersion(Version version);

    Version getLatestVersion();
}