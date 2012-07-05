package de.idos.updates;

public class UpdateSystem {
    private final Repository repository;
    private Version currentVersion;
    private Version latestVersion;

    public UpdateSystem(Repository repository) {
        this.repository = repository;
    }

    public void checkForUpdatesSinceVersion(Version version) {
        this.currentVersion = version;
        this.latestVersion = repository.getLatestVersion();
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
}