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
  UpdateConnection connection = mock(UpdateConnection.class);

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
    assertThat(createUpdateCheck().getInstalledVersion(), is(currentVersion));
  }

  private Version getLatestVersion() {
    UpdateCheck updateCheck = createUpdateCheck();
    return updateCheck.getLatestVersion();
  }

  private UpdateCheck createUpdateCheck() {
    return new UpdateCheck(connection);
  }
}
