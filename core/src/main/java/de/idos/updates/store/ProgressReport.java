package de.idos.updates.store;

import de.idos.updates.Version;

public interface ProgressReport {
    void lookingUpLatestAvailableVersion();

    void latestAvailableVersionIs(Version value);

    void versionLookupFailed(Exception e);

    void startingInstallationOf(Version version);

    void assemblingFileList();

    void foundElementsToInstall(int numberOfElements);

    void installingFile(String name);

    void expectedSize(long size);

    void progress(long progress);

    void finishedFile();

    void finishedInstallation();

    void installationFailed(Exception e);

    void updateAlreadyInProgress();
}