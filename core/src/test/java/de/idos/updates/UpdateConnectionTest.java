package de.idos.updates;

import de.idos.updates.repository.Repository;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UpdateConnectionTest {

  VersionStore versionStore = mock(VersionStore.class);
  Repository versionRepository = mock(Repository.class);
  UpdateConnection connection = new DefaultUpdateConnection(versionStore, versionRepository);

  @Test
  public void returnsInstalledVersion() throws Exception {
    Version currentVersion = new NumericVersion(1, 0, 0);
    when(versionStore.getLatestVersion()).thenReturn(currentVersion);
    Version version = connection.getLatestInstalledVersion();
    assertThat(version, is(currentVersion));
  }

  @Test
  public void returnsUpdateToAvailableVersion() throws Exception {
    Version latestVersion = new NumericVersion(1, 0, 1);
    when(versionRepository.getLatestVersion()).thenReturn(latestVersion);
    Update update = connection.getLatestAvailableUpdate();
    assertThat(update.getVersion(), is(latestVersion));
  }

  @Test
  public void returnsAvailableVersionFromDiscovery() throws Exception {
    VersionDiscovery discovery = mock(VersionDiscovery.class);
    new DefaultUpdateConnection(versionStore, discovery, new DefaultVersionInstaller(versionRepository, versionStore)).getLatestAvailableUpdate();
    verify(discovery).getLatestVersion();
  }

  @Test
  public void returnsInstalledVersionFromDiscovery() throws Exception {
    VersionDiscovery discovery = mock(VersionDiscovery.class);
    new DefaultUpdateConnection(discovery, versionRepository, new DefaultVersionInstaller(versionRepository, versionStore)).getLatestInstalledVersion();
    verify(discovery).getLatestVersion();
  }
}