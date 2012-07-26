package de.idos.updates;

public interface UpdateConnection {
  Version getLatestInstalledVersion();

  Version getLatestAvailableVersion();

  void install(Version latestVersion);
}
