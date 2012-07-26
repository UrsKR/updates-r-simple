package de.idos.updates;

import de.idos.updates.store.ProgressReport;

public interface VersionTransfer {
    void transferVersionTo(Version version, VersionStore store);

    void reportAllProgressTo(ProgressReport report);
}