package de.idos.updates;

public class UpdateConnection {

    private VersionStore versionStore;
    private VersionDiscovery discovery;
    private VersionTransfer transfer;

    public UpdateConnection(VersionStore versionStore, Repository versionRepository) {
        this(versionStore, versionRepository, versionRepository);
    }

    public UpdateConnection(VersionStore versionStore, VersionDiscovery discovery, VersionTransfer transfer) {
        this.versionStore = versionStore;
        this.discovery = discovery;
        this.transfer = transfer;
    }

    public Version getLatestInstalledVersion() {
        return versionStore.getLatestVersion();
    }

    public Version getLatestAvailableVersion() {
        return discovery.getLatestVersion();
    }

    public void install(Version latestVersion) {
        transfer.transferVersionTo(latestVersion, versionStore);
    }
}
