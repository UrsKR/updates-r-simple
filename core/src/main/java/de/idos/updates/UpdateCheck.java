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
    public Version getInstalledVersion() {
        return currentVersion;
    }

    @Override
    public Version getLatestVersion() {
        if (!latestVersionIsNewerThanInstalledVersion()) {
            return currentVersion;
        }
        return latestVersion;
    }

    @Override
    public void updateToLatestVersion() {
        if (latestVersionIsNewerThanInstalledVersion()) {
            updateConnection.install(latestVersion);
        }
    }

    private boolean latestVersionIsNewerThanInstalledVersion() {
        return hasUpdate() == Available;
    }
}