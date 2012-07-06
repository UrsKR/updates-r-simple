package de.idos.updates;

import static de.idos.updates.UpdateAvailability.Available;
import static de.idos.updates.UpdateAvailability.NotAvailable;

public class UpdateCheck implements Updater {
    private final Version currentVersion;
    private final Version latestVersion;
    private final VersionStore versionStore;
    private final Repository versionRepository;

    public UpdateCheck(VersionStore versionStore, Repository versionRepository) {
        this.versionStore = versionStore;
        this.versionRepository = versionRepository;
        this.currentVersion = versionStore.getLatestVersion();
        this.latestVersion = versionRepository.getLatestVersion();
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
            versionRepository.transferVersionTo(latestVersion, versionStore);
        }
    }
}