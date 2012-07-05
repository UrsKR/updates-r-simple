package de.idos.updates;

public class UpdateSystem {
    private VersionStore versionStore;
    private final Repository repository;
    private Version currentVersion;
    private Version latestVersion;

    public UpdateSystem(VersionStore versionStore, Repository repository) {
        this.versionStore = versionStore;
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

    public void updateToLatestVersion() {
        if (hasUpdate()) {
            repository.transferVersionTo(getLatestVersion(), versionStore);
        }
    }
}