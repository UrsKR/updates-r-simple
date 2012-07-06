package de.idos.updates;

public class UpdateSystem {
    private VersionStore versionStore;
    private final Repository repository;
    private UpdateCheck updateCheck;

    public UpdateSystem(VersionStore versionStore, Repository repository) {
        this.versionStore = versionStore;
        this.repository = repository;
    }

    public void checkForUpdatesSinceVersion(Version version) {
        this.updateCheck = new UpdateCheck(version, repository.getLatestVersion());
    }

    public boolean hasUpdate() {
        return updateCheck.hasUpdate();
    }

    public Version getLatestVersion() {
        return updateCheck.getLatestVersion();
    }

    public void updateToLatestVersion() {
        updateCheck.performUpdate(repository, versionStore);
    }

    public void removeOldVersions() {
        versionStore.removeOldVersions();
    }
}