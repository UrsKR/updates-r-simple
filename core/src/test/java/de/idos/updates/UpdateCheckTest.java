package de.idos.updates;

import de.idos.updates.store.NullInstallation;
import de.idos.updates.store.OngoingInstallation;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UpdateCheckTest {
  Version currentVersion = new NumericVersion(1, 0, 0);
  Version latestVersion = new NumericVersion(0, 0, 0);
  InstallableUpdate update = mock(InstallableUpdate.class);
  UpdateConnection connection = mock(DefaultUpdateConnection.class);

  @Before
  public void setCurrentVersion() throws Exception {
    when(connection.getLatestInstalledVersion()).thenReturn(currentVersion);
  }

  @Test
  public void returnsCurrentVersionIfNoNewerIsAvailable() throws Exception {
    when(connection.getLatestAvailableUpdate()).thenReturn(update);
    Version version = getLatestVersion();
    assertThat(version, is(currentVersion));
  }

  @Test
  public void doesNotChangeStateIfNewerVersionBecomesAvailableLater() throws Exception {
    when(connection.getLatestAvailableUpdate()).thenReturn(update);
    when(update.getVersion()).thenReturn(latestVersion);
    InstallableUpdate newUpdate = mock(InstallableUpdate.class);
    when(newUpdate.getVersion()).thenReturn(new NumericVersion(1, 1, 0));
    when(connection.getLatestAvailableUpdate()).thenReturn(update, newUpdate);
    Version version = getLatestVersion();
    assertThat(version, is(currentVersion));
  }

  @Test
  public void publishesCurrentlyInstalledVersion() throws Exception {
    assertThat(createExecutedUpdateCheck().getInstalledVersion(), is(currentVersion));
  }

  @Test(expected = IllegalStateException.class)
  public void expectsCheckToBeRunBeforeQueryingAvailability() throws Exception {
    UpdateCheck updateCheck = createUpdateCheck();
    updateCheck.hasUpdate();
  }

  @Test(expected = IllegalStateException.class)
  public void expectsCheckToBeRunBeforeInstallingUpdates() throws Exception {
    UpdateCheck updateCheck = createUpdateCheck();
    updateCheck.updateToLatestVersion();
  }

  @Test(expected = IllegalStateException.class)
  public void expectsCheckToBeRunBeforeQueryingLatestVersion() throws Exception {
    UpdateCheck updateCheck = createUpdateCheck();
    updateCheck.getLatestVersion();
  }

  @Test(expected = IllegalStateException.class)
  public void expectsCheckToBeRunBeforeQueryingInstalledVersion() throws Exception {
    UpdateCheck updateCheck = createUpdateCheck();
    updateCheck.getInstalledVersion();
  }

  @Test
  public void handsOutInstallationFromConnectionWhenNewerIsAvailable() throws Exception {
    InstallableUpdate update = mock(InstallableUpdate.class);
    when(update.isUpdateFrom(currentVersion)).thenReturn(UpdateAvailability.Available);
    when(connection.getLatestAvailableUpdate()).thenReturn(update);
    OngoingInstallation installation = mock(OngoingInstallation.class);
    when(update.install()).thenReturn(installation);
    UpdateCheck check = createExecutedUpdateCheck();
    OngoingInstallation actualInstallation = check.updateToLatestVersion();
    assertThat(actualInstallation, is(installation));
  }

  @Test
  public void returnsNullVersionOtherwise() throws Exception {
    when(connection.getLatestAvailableUpdate()).thenReturn(update);
    UpdateCheck check = createExecutedUpdateCheck();
    OngoingInstallation actualInstallation = check.updateToLatestVersion();
    assertThat(actualInstallation, is(instanceOf(NullInstallation.class)));
  }

  private Version getLatestVersion() {
    UpdateCheck updateCheck = createExecutedUpdateCheck();
    return updateCheck.getLatestVersion();
  }

  private UpdateCheck createExecutedUpdateCheck() {
    UpdateCheck updateCheck = createUpdateCheck();
    updateCheck.runCheck();
    return updateCheck;
  }

  private UpdateCheck createUpdateCheck() {
    return new UpdateCheck(connection);
  }
}
