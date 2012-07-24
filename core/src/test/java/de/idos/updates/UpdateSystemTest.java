package de.idos.updates;

import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class UpdateSystemTest {

    Version currentVersion = new NumericVersion(5, 0, 0);
    Version latestVersion = new NumericVersion(5, 3, 2);
    Repository repository = mock(Repository.class);
    VersionStore versionStore = mock(VersionStore.class);
    UpdateSystem updateSystem = new UpdateSystem(versionStore, repository);

    @Test
    public void hasNoUpdateIfThereIsNoNewVersion() throws Exception {
        installCurrentVersion();
        putVersionInRepository(currentVersion);
        assertThat(updateSystem.hasUpdate(), is(UpdateAvailability.NotAvailable));
    }

    @Test
    public void hasUpdateIfRepositoryContainsGreaterVersion() throws Exception {
        installCurrentVersion();
        putVersionInRepository(latestVersion);
        assertThat(updateSystem.hasUpdate(), is(UpdateAvailability.Available));
    }

    @Test
    public void handsOutUpdaterForLaterUse() throws Exception {
        installCurrentVersion();
        putVersionInRepository(latestVersion);
        assertThat(updateSystem.checkForUpdates().hasUpdate(), is(UpdateAvailability.Available));
    }

    @Test
    public void providesInfoAboutLatestVersion() throws Exception {
        installCurrentVersion();
        putVersionInRepository(latestVersion);
        assertThat(updateSystem.getLatestVersion(), is(latestVersion));
    }

    @Test
    public void triggersTransferOnRequest() throws Exception {
        installCurrentVersion();
        putVersionInRepository(latestVersion);
        updateSystem.updateToLatestVersion();
        verify(repository).transferVersionTo(latestVersion, versionStore);
    }

    @Test
    public void doesNotTriggerTransferIfNoUpdateAvailable() throws Exception {
        installCurrentVersion();
        putVersionInRepository(currentVersion);
        updateSystem.updateToLatestVersion();
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

    private void installCurrentVersion() {
        when(versionStore.getLatestVersion()).thenReturn(currentVersion);
    }

    private void putVersionInRepository(Version latestVersion) {
        when(repository.getLatestVersion()).thenReturn(latestVersion);
    }
}
