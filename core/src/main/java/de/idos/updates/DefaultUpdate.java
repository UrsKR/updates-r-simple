package de.idos.updates;

import de.idos.updates.store.OngoingInstallation;

import static de.idos.updates.UpdateAvailability.Available;
import static de.idos.updates.UpdateAvailability.NotAvailable;

public class DefaultUpdate implements Update {
  private final Version version;
  private final VersionInstaller installer;

  public DefaultUpdate(Version version, VersionInstaller installer) {
    this.version = version;
    this.installer = installer;
  }

  @Override
  public OngoingInstallation install() {
    return installer.install(version);
  }

  @Override
  public UpdateAvailability isUpdateFrom(Version currentVersion) {
    if (version.isGreaterThan(currentVersion)) {
      return Available;
    }
    return NotAvailable;
  }

  @Override
  public Version getVersion() {
    return version;
  }
}