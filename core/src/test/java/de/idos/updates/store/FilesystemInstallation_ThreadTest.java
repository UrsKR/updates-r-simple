package de.idos.updates.store;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.util.concurrent.CountDownLatch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

public class FilesystemInstallation_ThreadTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();
  private Installation installation;
  private ProgressReport report = mock(ProgressReport.class);
    final CountDownLatch latch = new CountDownLatch(1);

  @Before
  public void setUp() throws Exception {
    installation = FilesystemInstallation.create(folder.getRoot(), report);
  }

  @Test
  public void deletesFolderAfterInstallationOfCurrentElement() throws Exception {
    new Thread(new SlowInstallationRunnable()).start();
    latch.await();
    installation.abort();
    assertThat(folder.getRoot().exists(), is(false));
  }

  @Test
  public void waitsWithFinishingUntilCurrentElementIsInstalled() throws Exception {
    new Thread(new SlowInstallationRunnable()).start();
    latch.await();
    installation.finish();
    assertThat(InstallationUtil.getMarkerFile(folder.getRoot()).exists(), is(false));
  }

  private class SlowInstallationRunnable implements Runnable {

    @Override
    public void run() {
      installation.addContent(new SlowData(latch));
    }
  }
}