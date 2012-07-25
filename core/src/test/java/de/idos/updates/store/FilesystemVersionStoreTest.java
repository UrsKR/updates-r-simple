package de.idos.updates.store;

import de.idos.updates.NumericVersion;
import de.idos.updates.UpdateFailedException;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static de.idos.updates.NumericVersionMatchers.sameVersionAs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FilesystemVersionStoreTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private FilesystemVersionStore versionStore;
    private NumericVersion newVersion = new NumericVersion(1, 0, 0);

    @Before
    public void setUp() throws Exception {
        versionStore = new FilesystemVersionStore(folder.getRoot());
    }

    @Test
       public void createsVersionFolderWhenInstallationStarts() throws Exception {
        versionStore.beginInstallation(newVersion);
        assertThat(getVersionFolder().exists(), is(true));
    }

    @Test
    public void addsContentToVersionFolder() throws Exception {
        File contentFile = folder.newFile("ContentFile");
        Installation installation = versionStore.beginInstallation(newVersion);
        installation.addContent(new FileDataInVersion(contentFile));
        File versionFolder = getVersionFolder();
        File versionContentFile = new File(versionFolder, contentFile.getName());
        assertThat(versionContentFile.exists(), is(true));
    }

    @Test
    public void deletesOldVersions() throws Exception {
        NumericVersion oldVersion = new NumericVersion(0, 9, 0);
        versionStore.beginInstallation(oldVersion);
        File versionFolder = new File(folder.getRoot(), oldVersion.asString());
        File contentFile = new File(versionFolder, "ContentFile");
        contentFile.createNewFile();
        versionStore.beginInstallation(newVersion);
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
        NumericVersion version = new NumericVersion(0, 2, 0);
        versionStore.beginInstallation(new NumericVersion(0, 0, 8));
        versionStore.beginInstallation(version);
        assertThat(versionStore.getLatestVersion(), is(sameVersionAs(version)));
    }

    @Test
    public void publishesLatestVersionFolder() throws Exception {
        versionStore.beginInstallation(newVersion);
        assertThat(versionStore.getFolderForVersionToRun(), is(getVersionFolder()));
    }

    private File getVersionFolder() {
        return new File(folder.getRoot(), newVersion.asString());
    }
}