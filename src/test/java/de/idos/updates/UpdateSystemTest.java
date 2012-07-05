package de.idos.updates;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UpdateSystemTest {

    NumericVersion currentVersion = new NumericVersion(5, 0, 0);
    Repository repository = mock(Repository.class);
    VersionStore versionStore = mock(VersionStore.class);
    UpdateSystem updateSystem = new UpdateSystem(versionStore, repository);

    @Test
    public void hasUpdateIfRepositoryContainsGreaterVersion() throws Exception {
        putVersionInRepository(new NumericVersion(5, 3, 2));
        updateSystem.checkForUpdatesSinceVersion(currentVersion);
        assertThat(updateSystem.hasUpdate(), is(true));
    }

    @Test
    public void hasNoUpdateIfRepositoryHasNoNewVersion() throws Exception {
        putVersionInRepository(currentVersion);
        updateSystem.checkForUpdatesSinceVersion(currentVersion);
        assertThat(updateSystem.hasUpdate(), is(false));
    }

    @Test
    public void providesInfoAboutLatestVersion() throws Exception {
        Version latestVersion = new NumericVersion(5, 3, 2);
        putVersionInRepository(latestVersion);
        updateSystem.checkForUpdatesSinceVersion(currentVersion);
        assertThat(updateSystem.getLatestVersion(), is(latestVersion));
    }

    @Test
    public void triggersTransferOnRequest() throws Exception {
        Version latestVersion = new NumericVersion(5, 3, 2);
        putVersionInRepository(latestVersion);
        updateSystem.checkForUpdatesSinceVersion(currentVersion);
        updateSystem.updateToLatestVersion();
        verify(repository).transferVersionTo(latestVersion, versionStore);
    }

    @Test
    public void doesNotTriggerTransferIfNoUpdateAvailable() throws Exception {
        putVersionInRepository(currentVersion);
        updateSystem.checkForUpdatesSinceVersion(currentVersion);
        updateSystem.updateToLatestVersion();
        verify(repository, never()).transferVersionTo(any(Version.class), any(VersionStore.class));
    }

    private void putVersionInRepository(Version latestVersion) {
        when(repository.getLatestVersion()).thenReturn(latestVersion);
    }
}
