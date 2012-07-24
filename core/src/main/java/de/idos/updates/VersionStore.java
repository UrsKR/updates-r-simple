package de.idos.updates;

import de.idos.updates.Version;
import de.idos.updates.store.DataInVersion;
import de.idos.updates.store.UrlDataInVersion;

import java.io.File;
import java.net.URL;

public interface VersionStore {
    void addVersion(Version version);

    void addContent(Version version, DataInVersion dataInVersion);

    void removeOldVersions();

    void removeVersion(Version version);

    Version getLatestVersion();

    File getFolderForVersionToRun();
}