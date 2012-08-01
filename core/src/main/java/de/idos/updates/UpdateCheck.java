package de.idos.updates;

import de.idos.updates.store.NullInstallation;
import de.idos.updates.store.OngoingInstallation;

import static de.idos.updates.UpdateAvailability.Available;

public class UpdateCheck implements Updater {
  private final UpdateConnection updateConnection;
  private boolean checkHasRun = false;
  private Version currentVersion;
  private Update latestUpdate;

  public UpdateCheck(UpdateConnection updateConnection) {
    this.updateConnection = updateConnection;
  }

  @Override
  public UpdateAvailability hasUpdate() {
    assertCheckHasRun();
    return latestUpdate.isUpdateFrom(currentVersion);
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
    return latestUpdate.getVersion();
  }

  @Override
  public OngoingInstallation updateToLatestVersion() {
    assertCheckHasRun();
    if (latestVersionIsNewerThanInstalledVersion()) {
      return latestUpdate.install();
    }
    return new NullInstallation();
  }

  @Override
  public synchronized void runCheck() {
    if (!checkHasRun) {
      this.currentVersion = updateConnection.getLatestInstalledVersion();
      this.latestUpdate = updateConnection.getLatestAvailableUpdate();
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