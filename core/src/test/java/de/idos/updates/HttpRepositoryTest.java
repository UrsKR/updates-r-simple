package de.idos.updates;

import de.idos.updates.repository.HttpRepository;
import de.idos.updates.server.FileServer;
import de.idos.updates.store.FilesystemVersionStore;
import de.idos.updates.store.Installation;
import de.idos.updates.store.UrlDataInVersion;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Matchers;

import java.io.File;

import static de.idos.updates.NumericVersionMatchers.sameVersionAs;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HttpRepositoryTest {
    private static FileServer fileServer;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    HttpRepository repository = new HttpRepository("http://localhost:8080/");
    VersionStore store = mock(VersionStore.class);

    @BeforeClass
    public static void setUp() throws Exception {
        fileServer = new FileServer();
        fileServer.start();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        fileServer.stop();
    }

    @Test
    public void retrievesLatestVersionFromServer() throws Exception {
        Version latestVersion = repository.getLatestVersion();
        assertThat(latestVersion, is(sameVersionAs(new NumericVersion(5, 0, 4))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptBadUrls() throws Exception {
        new HttpRepository("xx");
    }

    @Test
    public void transfersVersionToStore() throws Exception {
        Installation installation = mock(Installation.class);
        NumericVersion version = new NumericVersion(5, 0, 4);
        when(store.beginInstallation(version)).thenReturn(installation);
        repository.transferVersionTo(version, store);
        verify(installation, times(2)).addContent(Matchers.isA(UrlDataInVersion.class));
    }

    @Test
    public void worksIfNoReporterIsRegistered() throws Exception {
        repository.transferVersionTo(new NumericVersion(5, 0, 4), new FilesystemVersionStore(folder.getRoot()));
        assertThat(new File(folder.getRoot(), "5.0.4").exists(), is(true));
    }
}