package de.idos.updates;

import java.util.concurrent.CountDownLatch;

public class ThreadTestUpdateConnection implements UpdateConnection {
  private Version v1;
  private Version v2;
  private boolean firstCall = true;

  public ThreadTestUpdateConnection(Version v1, Version v2) {
    this.v1 = v1;
    this.v2 = v2;
  }

  @Override
  public Version getLatestInstalledVersion() {
    return VersionFinder.BASE_VERSION;
  }

  @Override
  public Version getLatestAvailableVersion() {
    return getVersionOrWait();
  }

  @Override
  public void install(Version latestVersion) {
    throw new UnsupportedOperationException();
  }

  private Version getVersionOrWait() {
    if (firstCall) {
      try {
        firstCall = false;
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        //nothing to do
      }
      return v1;
    }
    return v2;
  }
}
