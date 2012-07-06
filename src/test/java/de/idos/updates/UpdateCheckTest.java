package de.idos.updates;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UpdateCheckTest {

    @Test
    public void returnsCurrentVersionIfNoNewerIsAvailable() throws Exception {
        Version currentVersion = new NumericVersion(1, 0, 0);
        Version latestVersion = new NumericVersion(0, 0, 0);
        Version version = new UpdateCheck(currentVersion, latestVersion).getLatestVersion();
        assertThat(version, is(currentVersion));
    }
}
