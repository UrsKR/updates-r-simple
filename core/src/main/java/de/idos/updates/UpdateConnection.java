package de.idos.updates;

public interface UpdateConnection {
  Version getLatestInstalledVersion();

  Version getLatestAvailableVersion();

  Update getLatestAvailableUpdate();
}