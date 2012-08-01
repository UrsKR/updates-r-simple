package de.idos.updates;

import de.idos.updates.repository.Repository;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultUpdateSystemTest {

  Version currentVersion = new NumericVersion(5, 0, 0);
  Version latestVersion = new NumericVersion(5, 3, 2);
  Repository repository = mock(Repository.class);
  VersionStore versionStore = mock(VersionStore.class);
  DefaultUpdateSystem updateSystem = new DefaultUpdateSystem(versionStore, versionStore, repository, repository);

  @Test
  public void hasNoUpdateIfThereIsNoNewVersion() throws Exception {
    installCurrentVersion();
    putVersionInRepository(currentVersion);
    assertThat(getUpdaterThatHasChecked().hasUpdate(), is(UpdateAvailability.NotAvailable));
  }

  @Test
  public void hasUpdateIfRepositoryContainsGreaterVersion() throws Exception {
    installCurrentVersion();
    putVersionInRepository(latestVersion);
    assertThat(getUpdaterThatHasChecked().hasUpdate(), is(UpdateAvailability.Available));
  }

  @Test
  public void handsOutUpdaterForLaterUse() throws Exception {
    installCurrentVersion();
    putVersionInRepository(latestVersion);
    assertThat(getUpdaterThatHasChecked().hasUpdate(), is(UpdateAvailability.Available));
  }

  @Test
  public void providesInfoAboutLatestVersion() throws Exception {
    installCurrentVersion();
    putVersionInRepository(latestVersion);
    assertThat(getUpdaterThatHasChecked().getLatestVersion(), is(latestVersion));
  }

  @Test
  public void triggersTransferOnRequest() throws Exception {
    installCurrentVersion();
    putVersionInRepository(latestVersion);
    getUpdaterThatHasChecked().updateToLatestVersion();
    verify(repository).transferVersionTo(latestVersion, versionStore);
  }

  @Test
  public void doesNotTriggerTransferIfNoUpdateAvailable() throws Exception {
    installCurrentVersion();
    putVersionInRepository(currentVersion);
    getUpdaterThatHasChecked().updateToLatestVersion();
    verify(repository, never()).transferVersionTo(any(Version.class), any(VersionStore.class));
  }

  @Test
  public void removesOldVersionsViaStore() throws Exception {
    updateSystem.removeOldVersions();
    verify(versionStore).removeOldVersions();
  }

  @Test
  public void findsFolderForLatestVersionViaStore() throws Exception {
    File value = new File(".");
    when(versionStore.getFolderForVersionToRun()).thenReturn(value);
    assertThat(updateSystem.getFolderForVersionToRun(), is(value));
  }

  @Test
  public void publishesCurrentlyInstalledVersion() throws Exception {
    installCurrentVersion();
    assertThat(updateSystem.getInstalledVersion(), is(currentVersion));
  }

  private void installCurrentVersion() {
    when(versionStore.getLatestVersion()).thenReturn(currentVersion);
  }

  private void putVersionInRepository(Version latestVersion) {
    when(repository.getLatestVersion()).thenReturn(latestVersion);
  }

  private Updater getUpdaterThatHasChecked() {
    Updater updater = updateSystem.checkForUpdates();
    updater.runCheck();
    return updater;
  }
}