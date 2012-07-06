package de.idos.updates;

public class UpdateSystem implements Updater {
    private final VersionStore versionStore;
    private final Repository repository;

    public UpdateSystem(VersionStore versionStore, Repository repository) {
        this.versionStore = versionStore;
        this.repository = repository;
    }

    public UpdateCheck checkForUpdates() {
        return new UpdateCheck(versionStore, repository);
    }

    @Override
    public UpdateAvailability hasUpdate() {
        return checkForUpdates().hasUpdate();
    }

    @Override
    public Version getLatestVersion() {
        return checkForUpdates().getLatestVersion();
    }

    @Override
    public void updateToLatestVersion() {
        checkForUpdates().updateToLatestVersion();
    }

    public void removeOldVersions() {
        versionStore.removeOldVersions();
    }
}