package de.idos.updates;

import static de.idos.updates.UpdateAvailability.Available;
import static de.idos.updates.UpdateAvailability.NotAvailable;

public class UpdateCheck {
    private Version currentVersion;
    private Version latestVersion;

    public UpdateCheck(Version currentVersion, Version latestVersion) {
        this.currentVersion = currentVersion;
        this.latestVersion = latestVersion;
    }

    public UpdateAvailability hasUpdate() {
        if (latestVersion.isGreaterThan(currentVersion)) {
            return Available;
        }
        return NotAvailable;
    }

    public Version getLatestVersion() {
        if (hasUpdate() != Available) {
            return currentVersion;
        }
        return latestVersion;
    }

    public void performUpdate(Repository repository, VersionStore store) {
        if (hasUpdate() == Available) {
            repository.transferVersionTo(latestVersion, store);
        }
    }
}