package de.idos.updates.install;

import de.idos.updates.Version;
import de.idos.updates.store.ProgressReport;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ThreadedInstallerTest {

  private Version version = mock(Version.class);
  private SlowInstaller wrapped = new SlowInstaller();
  private SlowInstaller wrappedSpy = spy(wrapped);
  private Installer installer = new ThreadedInstaller(wrappedSpy);

  @Test
  public void installsViaWrappedInstaller() throws Exception {
    installer.install(version);
    Thread.sleep(10);
    verify(wrappedSpy).install(version);
  }

  @Test
  public void startsInstallationInNewThread() throws Exception {
    installer.install(version);
    assertThat(wrapped.finished, is(false));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void createsDefaultInstallerForConvenience() throws Exception {
    ProgressReport report = mock(ProgressReport.class);
    InstallationStrategy strategy = mock(InstallationStrategy.class);
    ThreadedInstaller.PrepareInstallation(strategy, report).install(version);
    Thread.sleep(10);
    verify(report).startingInstallationOf(version);
  }
}