package de.idos.updates.install;

import de.idos.updates.Version;
import de.idos.updates.store.ProgressReport;

public class ThreadedInstaller implements Installer {
  public static <T> Installer PrepareInstallation(InstallationStrategy<T> strategy, ProgressReport report) {
    DefaultInstaller<T> wrapped = new DefaultInstaller<T>(strategy, report);
    return new ThreadedInstaller(wrapped);
  }

  private Installer wrapped;

  public ThreadedInstaller(Installer wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public void install(final Version version) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        wrapped.install(version);
      }
    }).start();
  }
}
