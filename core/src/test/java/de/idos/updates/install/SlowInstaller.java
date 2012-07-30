package de.idos.updates.install;

import de.idos.updates.Version;

public class SlowInstaller implements Installer {
  boolean finished = false;

  @Override
  public void install(Version version) {
    try {
      Thread.sleep(500);
      finished = true;
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}