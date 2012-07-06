package de.idos.updates;

import de.idos.updates.server.FileServer;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Matchers;

import java.net.URL;

import static de.idos.updates.NumericVersionMatchers.sameVersionAs;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class HttpRepositoryTest {
    HttpRepository repository = new HttpRepository("http://localhost:8080/");
    VersionStore store = mock(VersionStore.class);

    @Test
    public void retrievesLatestVersionFromServer() throws Exception {
        new FileServer().start();
        Version latestVersion = repository.getLatestVersion();
        assertThat(latestVersion, is(sameVersionAs(new NumericVersion(5, 0, 4))));
    }

    @Test
    public void returnsBaseVersionIfServerIsInaccessible() throws Exception {
        Version latestVersion = repository.getLatestVersion();
        assertThat(latestVersion, is(VersionFinder.BASE_VERSION));
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
        inOrder.verify(store, times(2)).addContent(eq(version), isA(String.class), Matchers.isA(URL.class));
    }

}