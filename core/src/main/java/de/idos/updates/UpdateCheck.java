package de.idos.updates;

public class UpdateCheck {
    private Version currentVersion;
    private Version latestVersion;

    public UpdateCheck(Version currentVersion, Version latestVersion) {
        this.currentVersion = currentVersion;
        this.latestVersion = latestVersion;
    }

    public boolean hasUpdate() {
        return latestVersion.isGreaterThan(currentVersion);
    }

    public Version getLatestVersion() {
        if (!hasUpdate()) {
            return currentVersion;
        }
        return latestVersion;
    }

    public void performUpdate(Repository repository, VersionStore store) {
        if (hasUpdate()) {
            repository.transferVersionTo(latestVersion, store);
        }
    }
}