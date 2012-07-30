package de.idos.updates.store;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.CountDownLatch;

public class SlowData implements DataInVersion {
  private CountDownLatch latch;

  public SlowData(CountDownLatch latch) {
    this.latch = latch;
  }

  @Override
  public void storeIn(File versionFolder) {
    try {
      FileOutputStream stream = new FileOutputStream(InstallationUtil.getMarkerFile(versionFolder));
      latch.countDown();
      Thread.sleep(500);
      stream.close();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
