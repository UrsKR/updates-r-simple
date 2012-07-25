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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FilesystemInstallationTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private FilesystemInstallation installation;
    private NumericVersion newVersion = new NumericVersion(1, 0, 0);

    @Before
    public void setUp() throws Exception {
        installation = new FilesystemInstallation(folder.getRoot());
    }

    @Test(expected = UpdateFailedException.class)
    public void throwsUpdateFailedExceptionOnError() throws Exception {
        File versionFolder = folder.getRoot();
        File contentFile = new File(versionFolder, "ContentFile");
        versionFolder.mkdir();
        contentFile.createNewFile();
        contentFile.setReadOnly();
        File newFile = folder.newFile("ContentFile");
        installation.addContent(new FileDataInVersion(newFile));
    }

    @Test(expected = UpdateFailedException.class)
    public void throwsUpdateFailedExceptionWhenURLCannotBeResolved() throws Exception {
        File contentFile = folder.newFile("ContentFile");
        contentFile.delete();
        installation.addContent(new UrlDataInVersion(contentFile.toURI().toURL(), "TheContent"));
    }

    @Test
    public void addsContentToVersionFolderFromURL() throws Exception {
        File contentFile = folder.newFile("ContentFile");
        FileUtils.writeStringToFile(contentFile, "XXX");
        installation.addContent(new UrlDataInVersion(contentFile.toURI().toURL(), "TheContent"));
        File versionFolder = folder.getRoot();
        File versionContentFile = new File(versionFolder, "TheContent");
        assertThat(FileUtils.readFileToString(versionContentFile), is("XXX"));
    }

    @Test
    public void deletesFolderWhenAbortingInstallation() throws Exception {
        installation.abort();
        assertThat(folder.getRoot().exists(), is(false));
    }

    @Ignore
    @Test
    public void marksInstallationInProgressInNewVersion() throws Exception {
        assertThat(new File(folder.getRoot(), "installation.running").exists(), is(true));
    }
}
