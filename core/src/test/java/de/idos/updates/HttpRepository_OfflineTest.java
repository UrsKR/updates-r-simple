package de.idos.updates;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HttpRepository_OfflineTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    HttpRepository repository = new HttpRepository("http://localhost:8080/");
    VersionStore store = mock(VersionStore.class);

    @Test
    public void returnsBaseVersionIfServerIsInaccessible() throws Exception {
        Version latestVersion = repository.getLatestVersion();
        assertThat(latestVersion, is(VersionFinder.BASE_VERSION));
    }

    @Test
    public void deletesVersionIfErrorsOccurDuringTransfer() throws Exception {
        NumericVersion version = new NumericVersion(5, 0, 4);
        repository.transferVersionTo(version, store);
        verify(store).removeVersion(version);
    }
}