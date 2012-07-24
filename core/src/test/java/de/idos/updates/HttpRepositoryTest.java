package de.idos.updates;

import de.idos.updates.server.FileServer;
import de.idos.updates.store.FilesystemVersionStore;
import de.idos.updates.store.ProgressReport;
import de.idos.updates.store.UrlDataInVersion;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.InOrder;
import org.mockito.Matchers;

import java.io.File;

import static de.idos.updates.NumericVersionMatchers.sameVersionAs;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

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
        NumericVersion version = new NumericVersion(5, 0, 4);
        repository.transferVersionTo(version, store);
        InOrder inOrder = inOrder(store);
        inOrder.verify(store).addVersion(version);
        inOrder.verify(store, times(2)).addContent(eq(version), Matchers.isA(UrlDataInVersion.class));
    }

    @Test
    public void worksIfNoReporterIsRegistered() throws Exception {
        repository.transferVersionTo(new NumericVersion(5, 0, 4), new FilesystemVersionStore(folder.getRoot()));
        assertThat(new File(folder.getRoot(), "5.0.4").exists(), is(true));
    }
}