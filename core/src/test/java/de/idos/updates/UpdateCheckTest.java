package de.idos.updates;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UpdateCheckTest {

    VersionStore versionStore = mock(VersionStore.class);
    Repository versionRepository = mock(Repository.class);

    @Test
    public void returnsCurrentVersionIfNoNewerIsAvailable() throws Exception {
        Version currentVersion = new NumericVersion(1, 0, 0);
        when(versionStore.getLatestVersion()).thenReturn(currentVersion);
        Version latestVersion = new NumericVersion(0, 0, 0);
        when(versionRepository.getLatestVersion()).thenReturn(latestVersion);
        UpdateCheck updateCheck = new UpdateCheck(versionStore, versionRepository);
        Version version = updateCheck.getLatestVersion();
        assertThat(version, is(currentVersion));
    }
}
