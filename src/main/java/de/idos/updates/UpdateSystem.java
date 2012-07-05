package de.idos.updates;

public class UpdateSystem {
    private final Repository repository;
    private Version version;
    private Version latestVersion;

    public UpdateSystem(Repository repository) {
        this.repository = repository;
    }

    public void checkForUpdatesSinceVersion(Version version) {
        this.version = version;
        this.latestVersion = repository.getLatestVersion();
    }

    public boolean hasUpdate() {
        return latestVersion.isGreaterThan(version);
    }

    public Version getLatestVersion() {
        return latestVersion;
    }
}