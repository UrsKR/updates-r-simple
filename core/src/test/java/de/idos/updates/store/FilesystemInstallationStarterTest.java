package de.idos.updates.store;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

public class FilesystemInstallationStarterTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();
  private final FilesystemInstallationStarter starter = new FilesystemInstallationStarter();
  private final ProgressReport report = mock(ProgressReport.class);

  @Test
  public void createsVersionFolderWhenInstallationStarts() throws Exception {
    File target = getTargetFolder();
    starter.start(target, report);
    assertThat(target.exists(), is(true));
  }

  @Test
  public void addsContentToVersionFolder() throws Exception {
    File contentFile = folder.newFile("ContentFile");
    File target = getTargetFolder();
    Installation installation = starter.start(target, report);
    installation.addContent(new FileDataInVersion(contentFile));
    File versionContentFile = new File(target, contentFile.getName());
    assertThat(versionContentFile.exists(), is(true));
  }

  private File getTargetFolder() {
    File root = folder.getRoot();
    return new File(root, "target");
  }

}
