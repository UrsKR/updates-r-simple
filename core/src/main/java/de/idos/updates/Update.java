package de.idos.updates;

public interface Update {

  UpdateAvailability isUpdateFrom(Version currentVersion);

  Version getVersion();
}