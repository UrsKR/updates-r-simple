package de.idos.updates.repository;

import de.idos.updates.NumericVersion;
import de.idos.updates.Version;
import de.idos.updates.VersionFinder;
import de.idos.updates.VersionStore;
import de.idos.updates.store.DataInVersion;
import de.idos.updates.store.Installation;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.CountDownLatch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpRepository_OfflineTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  HttpRepository repository = new HttpRepository("http://localhost:8081/");
  VersionStore store = mock(VersionStore.class);

  @Test
  public void returnsBaseVersionIfServerIsInaccessible() throws Exception {
    Version latestVersion = repository.getLatestVersion();
    assertThat(latestVersion, CoreMatchers.is(VersionFinder.BASE_VERSION));
  }

  @Test(timeout = 5000)
  public void deletesVersionIfErrorsOccurDuringTransfer() throws Exception {
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    NumericVersion version = new NumericVersion(5, 0, 4);
    Installation installation = mock(Installation.class);
    doThrow(new RuntimeException()).when(installation).addContent(isA(DataInVersion.class));
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        countDownLatch.countDown();
        return null;
      }
    }).when(installation).abort();
    when(store.beginInstallation(version)).thenReturn(installation);
    repository.transferVersionTo(version, store);
    countDownLatch.await();
  }
}