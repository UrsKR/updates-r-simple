package de.idos.updates.configuration;

import de.idos.updates.NullVersion;
import de.idos.updates.Version;
import de.idos.updates.VersionDiscovery;
import de.idos.updates.store.ProgressReport;

public class FallbackVersionDiscovery implements VersionDiscovery {
  private VersionDiscovery wrapped;
  private Version fallback;

  public FallbackVersionDiscovery(VersionDiscovery wrapped, Version fallback) {
    this.wrapped = wrapped;
    this.fallback = fallback;
  }

  @Override
  public Version getLatestVersion() {
    Version latestVersion = wrapped.getLatestVersion();
    if (latestVersion.isEqualTo(new NullVersion())) {
      return fallback;
    }
    return latestVersion;
  }

  @Override
  public void reportAllProgressTo(ProgressReport report) {
    wrapped.reportAllProgressTo(report);
  }
}