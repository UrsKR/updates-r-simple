package de.idos.updates.repository;

import de.idos.updates.NumericVersion;
import de.idos.updates.Version;
import de.idos.updates.VersionStore;
import de.idos.updates.integrationtest.SlowInstallation;
import de.idos.updates.store.FileDataInVersion;
import de.idos.updates.store.Installation;
import de.idos.updates.store.NullInstallation;
import de.idos.updates.store.OngoingInstallation;
import de.idos.updates.store.ProgressReport;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static de.idos.updates.NumericVersionMatchers.sameVersionAs;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FilesystemRepositoryTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();
  private File available_versions;
  private VersionStore store = mock(VersionStore.class);

  @Before
  public void fillRepository() throws Exception {
    available_versions = folder.newFolder("available_versions");
  }

  @Test
  public void reportsLatestVersionFromFile() throws Exception {
    addVersion("4.2.1");
    Version version = getLatestVersionFromRepository();
    assertThat(version, is(sameVersionAs(new NumericVersion(4, 2, 1))));
  }

  @Test
  public void reportsLatestVersionFromAllRegistered() throws Exception {
    addVersion("4.2.1");
    addVersion("4.2.2");
    Version version = getLatestVersionFromRepository();
    assertThat(version, is(sameVersionAs(new NumericVersion(4, 2, 2))));
  }

  @Test
  public void transfersVersionToStore() throws Exception {
    addVersion("4.2.1");
    File content = addContentToVersion("4.2.1", "content");
    NumericVersion version = new NumericVersion(4, 2, 1);
    Installation installation = mock(Installation.class);
    when(store.beginInstallation(version)).thenReturn(installation);
    new FilesystemRepository(folder.getRoot()).transferVersionTo(version, store);
    Thread.sleep(10);
    verify(installation).addContent(new FileDataInVersion(content));
  }

  @Test
  public void createsRepositoryFolderIfItDoesNotExist() throws Exception {
    available_versions.delete();
    new FilesystemRepository(folder.getRoot());
    assertThat(available_versions.exists(), is(true));
  }

  @Test
  public void runsInstallationInASeparateThread() throws Exception {
    addVersion("4.2.1");
    addContentToVersion("4.2.1", "content");
    NumericVersion version = new NumericVersion(4, 2, 1);
    CountDownLatch latch = new CountDownLatch(1);
    Installation installation = new SlowInstallation(new NullInstallation(), latch);
    when(store.beginInstallation(version)).thenReturn(installation);
    FilesystemRepository repository = new FilesystemRepository(folder.getRoot());
    ProgressReport report = mock(ProgressReport.class);
    repository.reportAllProgressTo(report);
    repository.transferVersionTo(version, store);
    verify(report, never()).finishedInstallation();
  }

  @Test
  public void handsOutInstallationControllerFromStore() throws Exception {
    NumericVersion version = new NumericVersion(4, 2, 1);
    Installation installation = mock(Installation.class);
    when(store.beginInstallation(version)).thenReturn(installation);
    FilesystemRepository repository = new FilesystemRepository(folder.getRoot());
    OngoingInstallation actualInstallation = repository.transferVersionTo(version, store);
    assertThat(actualInstallation, is((OngoingInstallation) installation));
  }

  private File addContentToVersion(String versionNumber, String fileName) throws IOException {
    File versionFolder = new File(available_versions, versionNumber);
    File file = new File(versionFolder, fileName);
    file.createNewFile();
    return file;
  }

  private void addVersion(String versionNumber) throws IOException {
    new File(available_versions, versionNumber).mkdir();
  }

  private Version getLatestVersionFromRepository() {
    FilesystemRepository repository = new FilesystemRepository(folder.getRoot());
    return repository.getLatestVersion();
  }
}