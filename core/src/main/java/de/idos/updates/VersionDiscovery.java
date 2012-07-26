package de.idos.updates;

import de.idos.updates.store.ProgressReport;

public interface VersionDiscovery {
    Version getLatestVersion();

    void reportAllProgressTo(ProgressReport report);
}