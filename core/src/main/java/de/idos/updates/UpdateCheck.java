package de.idos.updates;

import de.idos.updates.store.NullInstallation;
import de.idos.updates.store.OngoingInstallation;

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
    assertCheckHasRun();
    if (latestVersion.isGreaterThan(currentVersion)) {
      return Available;
    }
    return NotAvailable;
  }

  @Override
  public Version getInstalledVersion() {
    assertCheckHasRun();
    return currentVersion;
  }

  @Override
  public Version getLatestVersion() {
    assertCheckHasRun();
    if (!latestVersionIsNewerThanInstalledVersion()) {
      return currentVersion;
    }
    return latestVersion;
  }

  @Override
  public OngoingInstallation updateToLatestVersion() {
    assertCheckHasRun();
    if (latestVersionIsNewerThanInstalledVersion()) {
      return updateConnection.install(latestVersion);
    }
    return new NullInstallation();
  }

  @Override
  public synchronized void runCheck() {
    if (!checkHasRun) {
      this.currentVersion = updateConnection.getLatestInstalledVersion();
      this.latestVersion = updateConnection.getLatestAvailableVersion();
      this.checkHasRun = true;
    }
  }

  private boolean latestVersionIsNewerThanInstalledVersion() {
    return hasUpdate() == Available;
  }

  private void assertCheckHasRun() {
    if (!checkHasRun) {
      throw new IllegalStateException("The check for updates has not yet run. Please execute it by calling 'runCheck()' before calling other methods.");
    }
  }
}