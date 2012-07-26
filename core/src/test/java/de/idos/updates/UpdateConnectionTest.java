package de.idos.updates;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UpdateConnectionTest {

  VersionStore versionStore = mock(VersionStore.class);
  Repository versionRepository = mock(Repository.class);
  UpdateConnection connection = new UpdateConnection(versionStore, versionRepository);

  @Test
  public void returnsInstalledVersion() throws Exception {
    Version currentVersion = new NumericVersion(1, 0, 0);
    when(versionStore.getLatestVersion()).thenReturn(currentVersion);
    Version version = connection.getLatestInstalledVersion();
    assertThat(version, is(currentVersion));
  }

  @Test
  public void returnsAvailableVersion() throws Exception {
    Version latestVersion = new NumericVersion(1, 0, 1);
    when(versionRepository.getLatestVersion()).thenReturn(latestVersion);
    Version version = connection.getLatestAvailableVersion();
    assertThat(version, is(latestVersion));
  }

  @Test
  public void returnsAvailableVersionFromDiscovery() throws Exception {
    VersionDiscovery discovery = mock(VersionDiscovery.class);
    new UpdateConnection(versionStore, versionStore, discovery, versionRepository).getLatestAvailableVersion();
    verify(discovery).getLatestVersion();
  }

  @Test
  public void returnsInstalledVersionFromDiscovery() throws Exception {
    VersionDiscovery discovery = mock(VersionDiscovery.class);
    new UpdateConnection(discovery, versionStore, versionRepository, versionRepository).getLatestInstalledVersion();
    verify(discovery).getLatestVersion();
  }

  @Test
  public void installsVersionFromRepositoryToStore() throws Exception {
    NumericVersion latestVersion = new NumericVersion(1, 0, 0);
    connection.install(latestVersion);
    verify(versionRepository).transferVersionTo(latestVersion, versionStore);
  }
}