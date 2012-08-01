package de.idos.updates;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UpdateConnectionTest {

  VersionDiscovery installedDiscovery = mock(VersionDiscovery.class);
  VersionDiscovery availableDiscovery = mock(VersionDiscovery.class);
  VersionInstaller versionInstaller = mock(VersionInstaller.class);
  UpdateConnection connection = new DefaultUpdateConnection(installedDiscovery, availableDiscovery, versionInstaller);

  @Test
  public void returnsInstalledVersion() throws Exception {
    Version currentVersion = new NumericVersion(1, 0, 0);
    when(installedDiscovery.getLatestVersion()).thenReturn(currentVersion);
    Version version = connection.getLatestInstalledVersion();
    assertThat(version, is(currentVersion));
  }

  @Test
  public void returnsUpdateToAvailableVersion() throws Exception {
    Version latestVersion = new NumericVersion(1, 0, 1);
    when(availableDiscovery.getLatestVersion()).thenReturn(latestVersion);
    InstallableUpdate update = connection.getLatestAvailableUpdate();
    assertThat(update.getVersion(), is(latestVersion));
  }

  @Test
  public void updateWillInstallViaInstaller() throws Exception {
    Version latestVersion = new NumericVersion(1, 0, 1);
    when(availableDiscovery.getLatestVersion()).thenReturn(latestVersion);
    InstallableUpdate update = connection.getLatestAvailableUpdate();
    update.install();
    verify(versionInstaller).install(latestVersion);
  }
}