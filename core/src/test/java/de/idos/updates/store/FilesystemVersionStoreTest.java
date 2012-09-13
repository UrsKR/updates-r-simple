package de.idos.updates.store;

import de.idos.updates.NumericVersion;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Matchers;

import java.io.File;

import static de.idos.updates.NumericVersionMatchers.sameVersionAs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FilesystemVersionStoreTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private FilesystemVersionStore versionStore;
    private final NumericVersion newVersion = new NumericVersion(1, 0, 0);
    private final InstallationStarter starter = spy(new FilesystemInstallationStarter());

    @Before
    public void setUp() throws Exception {
        versionStore = new FilesystemVersionStore(folder.getRoot(), starter);
    }

    @Test
    public void startsInstallationViaInstallationStarter() throws Exception {
        versionStore.beginInstallation(newVersion);
        verify(starter).start(eq(getVersionFolder()), Matchers.isA(NullReport.class));
    }

    @Test
    public void reportsProgressToGivenReport() throws Exception {
        ProgressReport report = mock(ProgressReport.class);
        versionStore.reportAllProgressTo(report);
        versionStore.beginInstallation(newVersion);
        verify(starter).start(getVersionFolder(), report);
    }

    @Test
    public void deletesOldVersions() throws Exception {
        NumericVersion oldVersion = new NumericVersion(0, 9, 0);
        versionStore.beginInstallation(oldVersion).finish();
        File versionFolder = new File(folder.getRoot(), oldVersion.asString());
        File contentFile = new File(versionFolder, "ContentFile");
        contentFile.createNewFile();
        versionStore.beginInstallation(newVersion).finish();
        versionStore.removeOldVersions();
        assertThat(new File(folder.getRoot(), oldVersion.asString()).exists(), is(false));
    }

    @Test
    public void keepsNewVersion() throws Exception {
        NumericVersion oldVersion = new NumericVersion(0, 9, 0);
        versionStore.beginInstallation(oldVersion);
        versionStore.beginInstallation(newVersion);
        versionStore.removeOldVersions();
        assertThat(getVersionFolder().exists(), is(true));
    }

    @Test
    public void identifiesLatestVersion() throws Exception {
        NumericVersion newVersion = new NumericVersion(0, 2, 0);
        NumericVersion oldVersion = new NumericVersion(0, 0, 8);
        versionStore.beginInstallation(oldVersion).finish();
        versionStore.beginInstallation(newVersion).finish();
        assertThat(versionStore.getLatestVersion(), is(sameVersionAs(newVersion)));
    }

    @Test
    public void publishesLatestVersionFolder() throws Exception {
        versionStore.beginInstallation(newVersion).finish();
        assertThat(versionStore.getFolderForVersionToRun(), is(getVersionFolder()));
    }

    private File getVersionFolder() {
        return new File(folder.getRoot(), newVersion.asString());
    }
}