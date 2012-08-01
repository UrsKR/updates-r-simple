package de.idos.updates;

import static de.idos.updates.UpdateAvailability.Available;
import static de.idos.updates.UpdateAvailability.NotAvailable;

public class UpdateDescription implements Update{
  private Version version;

  public UpdateDescription(Version version) {
    this.version = version;
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