package de.idos.updates;

public interface UpdateConnection {
  Version getLatestInstalledVersion();

  InstallableUpdate getLatestAvailableUpdate();
}