package de.idos.updates;

import static de.idos.updates.UpdateAvailability.Available;
import static de.idos.updates.UpdateAvailability.NotAvailable;

public class UpdateCheck implements Updater {
    private final Version currentVersion;
    private final Version latestVersion;
    private final UpdateConnection updateConnection;

    public UpdateCheck(UpdateConnection updateConnection) {
        this.updateConnection = updateConnection;
        this.currentVersion = updateConnection.getLatestInstalledVersion();
        this.latestVersion = updateConnection.getLatestAvailableVersion();
    }

    @Override
    public UpdateAvailability hasUpdate() {
        if (latestVersion.isGreaterThan(currentVersion)) {
            return Available;
        }
        return NotAvailable;
    }

    @Override
    public Version getLatestVersion() {
        if (hasUpdate() != Available) {
            return currentVersion;
        }
        return latestVersion;
    }

    @Override
    public void updateToLatestVersion() {
        if (hasUpdate() == Available) {
            updateConnection.install(latestVersion);
        }
    }
}