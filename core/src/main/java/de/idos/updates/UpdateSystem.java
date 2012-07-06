package de.idos.updates;

public class UpdateSystem {
    private VersionStore versionStore;
    private final Repository repository;
    private UpdateCheck updateCheck;

    public UpdateSystem(VersionStore versionStore, Repository repository) {
        this.versionStore = versionStore;
        this.repository = repository;
    }

    public void checkForUpdates() {
        checkForUpdatesSinceVersion(versionStore.getLatestVersion());
    }

    public boolean hasUpdate() {
        checkForUpdatesIfItHasNotYetHappened();
        return updateCheck.hasUpdate();
    }

    public Version getLatestVersion() {
        checkForUpdatesIfItHasNotYetHappened();
        return updateCheck.getLatestVersion();
    }

    public void updateToLatestVersion() {
        checkForUpdatesIfItHasNotYetHappened();
        updateCheck.performUpdate(repository, versionStore);
    }

    public void removeOldVersions() {
        versionStore.removeOldVersions();
    }

    private void checkForUpdatesSinceVersion(Version version) {
        this.updateCheck = new UpdateCheck(version, repository.getLatestVersion());
    }

    private void checkForUpdatesIfItHasNotYetHappened() {
        if (updateCheck == null){
            checkForUpdates();
        }
    }
}