package de.idos.updates;

public interface Repository {
    Version getLatestVersion();

    void transferVersionTo(Version version, VersionStore store);
}
