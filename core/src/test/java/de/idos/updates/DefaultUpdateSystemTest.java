package de.idos.updates;

import de.idos.updates.store.ProgressReport;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class DefaultUpdateSystemTest {

    Version currentVersion = new NumericVersion(5, 0, 0);
    Version latestVersion = new NumericVersion(5, 3, 2);
    Repository repository = mock(Repository.class);
    VersionStore versionStore = mock(VersionStore.class);
    DefaultUpdateSystem updateSystem = new DefaultUpdateSystem(versionStore, repository);

    @Test
    public void hasNoUpdateIfThereIsNoNewVersion() throws Exception {
        installCurrentVersion();
        putVersionInRepository(currentVersion);
        assertThat(updateSystem.checkForUpdates().hasUpdate(), is(UpdateAvailability.NotAvailable));
    }

    @Test
    public void hasUpdateIfRepositoryContainsGreaterVersion() throws Exception {
        installCurrentVersion();
        putVersionInRepository(latestVersion);
        assertThat(updateSystem.checkForUpdates().hasUpdate(), is(UpdateAvailability.Available));
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
        assertThat(updateSystem.checkForUpdates().getLatestVersion(), is(latestVersion));
    }

    @Test
    public void triggersTransferOnRequest() throws Exception {
        installCurrentVersion();
        putVersionInRepository(latestVersion);
        updateSystem.checkForUpdates().updateToLatestVersion();
        verify(repository).transferVersionTo(latestVersion, versionStore);
    }

    @Test
    public void doesNotTriggerTransferIfNoUpdateAvailable() throws Exception {
        installCurrentVersion();
        putVersionInRepository(currentVersion);
        updateSystem.checkForUpdates().updateToLatestVersion();
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
    public void installsReportOnRepository() throws Exception {
        ProgressReport report = mock(ProgressReport.class);
        updateSystem.reportAllProgressTo(report);
        verify(repository).reportAllProgressTo(report);
    }

    private void installCurrentVersion() {
        when(versionStore.getLatestVersion()).thenReturn(currentVersion);
    }

    private void putVersionInRepository(Version latestVersion) {
        when(repository.getLatestVersion()).thenReturn(latestVersion);
    }
}
