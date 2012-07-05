package de.idos.updates;

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
}
