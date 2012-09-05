package de.idos.updates;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UpdateCheck_ThreadTest {
  Version v1 = new NumericVersion(1, 0, 0);
  Version v2 = new NumericVersion(1, 1, 0);
  UpdateConnection connection = new ThreadTestUpdateConnection(v1, v2);
  private CountDownLatch assertionLatch = new CountDownLatch(2);
  private Version foundVersion;

  @Test
  public void doesNotChangeStateIfNewerVersionBecomesAvailableLater() throws Exception {
    UpdateCheck updateCheck = new UpdateCheck(connection);
    new Thread(new LatestVersionCheck(updateCheck)).start();
    new Thread(new LatestVersionCheck(updateCheck)).start();
    assertionLatch.await();
    assertThat(foundVersion, is(v1));
  }

  private void setVersion(Version latestVersion) {
    if (foundVersion == null) {
      foundVersion = latestVersion;
    }
  }

  private class LatestVersionCheck implements Runnable {
    private final UpdateCheck updateCheck;

    public LatestVersionCheck(UpdateCheck updateCheck) {
      this.updateCheck = updateCheck;
    }

    @Override
    public void run() {
      updateCheck.runCheck();
      setVersion(updateCheck.getLatestVersion());
      assertionLatch.countDown();
    }
  }
}