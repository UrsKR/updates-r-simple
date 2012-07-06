package de.idos.updates;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

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
    public void createsVersionFolder() throws Exception {
        versionStore.addVersion(newVersion);
        assertThat(new File(folder.getRoot(), newVersion.asString()).exists(), is(true));
    }

    @Test
    public void addsContentToVersionFolder() throws Exception {
        File contentFile = folder.newFile("ContentFile");
        versionStore.addVersion(newVersion);
        versionStore.addContent(newVersion, contentFile);
        File versionFolder = new File(folder.getRoot(), newVersion.asString());
        File versionContentFile = new File(versionFolder, contentFile.getName());
        assertThat(versionContentFile.exists(), is(true));
    }

    @Test
    public void addsContentToVersionFolderFromURL() throws Exception {
        File contentFile = folder.newFile("ContentFile");
        FileUtils.writeStringToFile(contentFile, "XXX");
        versionStore.addVersion(newVersion);
        versionStore.addContent(newVersion, "TheContent", contentFile.toURI().toURL());
        File versionFolder = new File(folder.getRoot(), newVersion.asString());
        File versionContentFile = new File(versionFolder, "TheContent");
        assertThat(FileUtils.readFileToString(versionContentFile), is("XXX"));
    }

    @Test
    public void deletesOldVersions() throws Exception {
        NumericVersion oldVersion = new NumericVersion(0, 9, 0);
        versionStore.addVersion(oldVersion);
        File versionFolder = new File(folder.getRoot(), oldVersion.asString());
        File contentFile = new File(versionFolder, "ContentFile");
        contentFile.createNewFile();
        versionStore.addVersion(newVersion);
        versionStore.removeOldVersions();
        assertThat(new File(folder.getRoot(), oldVersion.asString()).exists(), is(false));
    }

    @Test
    public void deletesSpecificVersion() throws Exception {
        NumericVersion oldVersion = new NumericVersion(0, 9, 0);
        versionStore.addVersion(oldVersion);
        versionStore.removeVersion(oldVersion);
        assertThat(new File(folder.getRoot(), oldVersion.asString()).exists(), is(false));
    }

    @Test
    public void keepsNewVersion() throws Exception {
        NumericVersion oldVersion = new NumericVersion(0, 9, 0);
        versionStore.addVersion(oldVersion);
        versionStore.addVersion(newVersion);
        versionStore.removeOldVersions();
        assertThat(new File(folder.getRoot(), newVersion.asString()).exists(), is(true));
    }

    @Test(expected = UpdateFailedException.class)
    public void throwsUpdateFailedExceptionOnError() throws Exception {
        File versionFolder = new File(folder.getRoot(), newVersion.asString());
        File contentFile = new File(versionFolder, "ContentFile");
        versionFolder.mkdir();
        contentFile.createNewFile();
        contentFile.setReadOnly();
        File newFile = folder.newFile("ContentFile");
        versionStore.addVersion(newVersion);
        versionStore.addContent(newVersion, newFile);
    }

    @Test(expected = UpdateFailedException.class)
    public void throwsUpdateFailedExceptionWhenURLCannotBeResolved() throws Exception {
        File contentFile = folder.newFile("ContentFile");
        versionStore.addVersion(newVersion);
        contentFile.delete();
        versionStore.addContent(newVersion, "TheContent", contentFile.toURI().toURL());
    }
}