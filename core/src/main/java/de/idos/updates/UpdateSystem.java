package de.idos.updates;

import de.idos.updates.store.ProgressReport;

import java.io.File;

public interface UpdateSystem {
    Updater checkForUpdates();

    void removeOldVersions();

    void reportAllProgressTo(ProgressReport report);

    File getFolderForVersionToRun();

    Version getInstalledVersion();

    void stopReportingTo(ProgressReport report);
}
