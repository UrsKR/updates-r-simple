package de.idos.updates.store;

import de.idos.updates.server.FileServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UrlStreamFactoryTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private static FileServer fileServer;

    @BeforeClass
    public static void setUp() throws Exception {
        fileServer = new FileServer();
        fileServer.start();
    }

    @Test
    public void reportsContentLengthAsSize() throws Exception {
        URL url = new URL("http://localhost:8080/updates/availableVersions");
        long expectedSize = new UrlStreamFactory(url).getExpectedSize();
        assertThat(expectedSize, is(12L));
    }

    @AfterClass
    public static void tearDown() throws Exception {
          fileServer.stop();
    }
}
