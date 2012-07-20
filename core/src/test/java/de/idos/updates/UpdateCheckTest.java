package de.idos.updates;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UpdateCheckTest {

    UpdateConnection connection = mock(UpdateConnection.class);

    @Test
    public void returnsCurrentVersionIfNoNewerIsAvailable() throws Exception {
        Version currentVersion = new NumericVersion(1, 0, 0);
        when(connection.getLatestInstalledVersion()).thenReturn(currentVersion);
        Version latestVersion = new NumericVersion(0, 0, 0);
        when(connection.getLatestAvailableVersion()).thenReturn(latestVersion);
        UpdateCheck updateCheck = new UpdateCheck(connection);
        Version version = updateCheck.getLatestVersion();
        assertThat(version, is(currentVersion));
    }
}
