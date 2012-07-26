package de.idos.updates;

import static de.idos.updates.UpdateAvailability.Available;
import static de.idos.updates.UpdateAvailability.NotAvailable;

public class UpdateCheck implements Updater {
  private boolean checkHasRun = false;
  private Version currentVersion;
  private Version latestVersion;
  private final UpdateConnection updateConnection;

  public UpdateCheck(UpdateConnection updateConnection) {
    this.updateConnection = updateConnection;
  }

  @Override
  public UpdateAvailability hasUpdate() {
    runCheck();
    if (latestVersion.isGreaterThan(currentVersion)) {
      return Available;
    }
    return NotAvailable;
  }

  @Override
  public Version getInstalledVersion() {
    runCheck();
    return currentVersion;
  }

  @Override
  public Version getLatestVersion() {
    runCheck();
    if (!latestVersionIsNewerThanInstalledVersion()) {
      return currentVersion;
    }
    return latestVersion;
  }

  @Override
  public void updateToLatestVersion() {
    runCheck();
    if (latestVersionIsNewerThanInstalledVersion()) {
      updateConnection.install(latestVersion);
    }
  }

  @Override
  public void runCheck() {
    if (!checkHasRun) {
      this.currentVersion = updateConnection.getLatestInstalledVersion();
      this.latestVersion = updateConnection.getLatestAvailableVersion();
      this.checkHasRun = true;
    }
  }

  private boolean latestVersionIsNewerThanInstalledVersion() {
    return hasUpdate() == Available;
  }
}