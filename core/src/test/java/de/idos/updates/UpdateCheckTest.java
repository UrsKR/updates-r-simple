package de.idos.updates;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UpdateCheckTest {
    Version currentVersion = new NumericVersion(1, 0, 0);
    Version latestVersion = new NumericVersion(0, 0, 0);
    UpdateConnection connection = mock(UpdateConnection.class);

    @Test
    public void returnsCurrentVersionIfNoNewerIsAvailable() throws Exception {
        when(connection.getLatestInstalledVersion()).thenReturn(currentVersion);
        when(connection.getLatestAvailableVersion()).thenReturn(latestVersion);
        UpdateCheck updateCheck = new UpdateCheck(connection);
        Version version = updateCheck.getLatestVersion();
        assertThat(version, is(currentVersion));
    }

    @Test
    public void doesNotChangeStateIfNewerVersionBecomesAvailableLater() throws Exception {
        when(connection.getLatestInstalledVersion()).thenReturn(currentVersion);
        when(connection.getLatestAvailableVersion()).thenReturn(latestVersion, new NumericVersion(1,1,0));
        UpdateCheck updateCheck = new UpdateCheck(connection);
        Version version = updateCheck.getLatestVersion();
        assertThat(version, is(currentVersion));
    }
}
