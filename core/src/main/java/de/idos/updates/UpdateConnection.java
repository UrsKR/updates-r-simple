package de.idos.updates;

public class UpdateConnection {

    private VersionStore versionStore;
    private Repository versionRepository;

    public UpdateConnection(VersionStore versionStore, Repository versionRepository) {
        this.versionStore = versionStore;
        this.versionRepository = versionRepository;
    }

    public Version getLatestInstalledVersion() {
        return versionStore.getLatestVersion();
    }

    public Version getLatestAvailableVersion() {
        return versionRepository.getLatestVersion();
    }

    public void install(Version latestVersion) {
        versionRepository.transferVersionTo(latestVersion, versionStore);
    }
}
