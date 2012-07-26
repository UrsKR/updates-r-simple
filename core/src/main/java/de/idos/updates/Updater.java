package de.idos.updates;

public interface Updater {
    UpdateAvailability hasUpdate();

    Version getLatestVersion();

    void updateToLatestVersion();

    Version getInstalledVersion();
}