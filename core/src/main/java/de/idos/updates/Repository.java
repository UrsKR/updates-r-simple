package de.idos.updates;

import de.idos.updates.store.ProgressReport;

public interface Repository {
    Version getLatestVersion();

    void transferVersionTo(Version version, VersionStore store);

    void reportAllProgressTo(ProgressReport report);
}