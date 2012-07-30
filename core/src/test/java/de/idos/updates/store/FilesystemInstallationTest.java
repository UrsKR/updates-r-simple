package de.idos.updates.store;

import de.idos.updates.UpdateFailedException;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static de.idos.updates.store.InstallationUtil.hasMarkerFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

public class FilesystemInstallationTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();
  private Installation installation;
  private ProgressReport report = mock(ProgressReport.class);

  @Before
  public void setUp() throws Exception {
    installation = FilesystemInstallation.create(folder.getRoot(), report);
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

  @Test
  public void marksInstallationInProgressInNewVersion() throws Exception {
    assertThat(hasMarkerFile(folder.getRoot()), is(true));
  }

  @Test
  public void removesProgressMarkerWhenDone() throws Exception {
    installation.finish();
    assertThat(hasMarkerFile(folder.getRoot()), is(false));
  }

  @Test
  public void isOngoingWhileNotFinished() throws Exception {
    assertThat(installation.isRunning(), is(true));
  }

  @Test
  public void isNoLongerRunningWhenFinished() throws Exception {
    installation.finish();
    assertThat(installation.isRunning(), is(false));
  }

  @Test
  public void isNoLongerRunningWhenAborted() throws Exception {
    installation.abort();
    assertThat(installation.isRunning(), is(false));
  }

  @Test
  public void doesNotAddContentWhenNotRunning() throws Exception {
    installation.finish();
    File contentFile = folder.newFile("ContentFile");
    FileUtils.writeStringToFile(contentFile, "XXX");
    installation.addContent(new UrlDataInVersion(contentFile.toURI().toURL(), "TheContent"));
    File versionFolder = folder.getRoot();
    File versionContentFile = new File(versionFolder, "TheContent");
    assertThat(versionContentFile.exists(), is(false));
  }
}