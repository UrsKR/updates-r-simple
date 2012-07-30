package de.idos.updates;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UpdateCheckTest {
  Version currentVersion = new NumericVersion(1, 0, 0);
  Version latestVersion = new NumericVersion(0, 0, 0);
  UpdateConnection connection = mock(DefaultUpdateConnection.class);

  @Before
  public void setCurrentVersion() throws Exception {
    when(connection.getLatestInstalledVersion()).thenReturn(currentVersion);
  }

  @Test
  public void returnsCurrentVersionIfNoNewerIsAvailable() throws Exception {
    when(connection.getLatestAvailableVersion()).thenReturn(latestVersion);
    Version version = getLatestVersion();
    assertThat(version, is(currentVersion));
  }

  @Test
  public void doesNotChangeStateIfNewerVersionBecomesAvailableLater() throws Exception {
    when(connection.getLatestAvailableVersion()).thenReturn(latestVersion, new NumericVersion(1, 1, 0));
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
