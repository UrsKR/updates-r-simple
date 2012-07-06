package de.idos.updates;

import de.idos.updates.server.FileServer;
import org.junit.Test;

import static de.idos.updates.NumericVersionMatchers.sameVersionAs;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HttpRepositoryTest {

    @Test
    public void retrievesLatestVersionFromServer() throws Exception {
        new FileServer().start();
        HttpRepository repository = new HttpRepository("http://localhost:8080/updates");
        Version latestVersion = repository.getLatestVersion();
        assertThat(latestVersion, is(sameVersionAs(new NumericVersion(5, 0, 4))));
    }
}