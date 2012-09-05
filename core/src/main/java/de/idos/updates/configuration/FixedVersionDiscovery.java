package de.idos.updates.configuration;

import de.idos.updates.Version;
import de.idos.updates.VersionDiscovery;
import de.idos.updates.store.ProgressReport;

public class FixedVersionDiscovery implements VersionDiscovery {
  private Version version;

  public FixedVersionDiscovery(Version version) {
    this.version = version;
  }

  @Override
  public Version getLatestVersion() {
    return version;
  }

  @Override
  public void reportAllProgressTo(ProgressReport report) {
    //nothing to report
  }
}
