package de.idos.updates.install;

import de.idos.updates.NumericVersion;
import de.idos.updates.server.FileServer;
import de.idos.updates.store.FilesystemInstallation;
import de.idos.updates.store.Installation;
import de.idos.updates.store.NullInstallation;
import de.idos.updates.store.NullReport;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class HttpInstaller_HardcodingTest {
  private static FileServer fileServer;

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @BeforeClass
  public static void setUp() throws Exception {
    fileServer = new FileServer();
    fileServer.start();
  }

  @Test
  public void extendsBaseUrlWhenListingContent() throws Exception {
    HttpInstaller installer = createInstaller(new NullInstallation());
    List<String> elementsToInstall = installer.findAllElementsToInstall(new NumericVersion(5, 0, 4));
    assertThat(elementsToInstall.size(), is(2));
  }

  @Test
  public void extendsBaseUrlWhenDownloadingElements() throws Exception {
    Installation installation = FilesystemInstallation.create(folder.getRoot(), new NullReport());
    HttpInstaller installer = createInstaller(installation);
    installer.installElement("fileToUpdate", new NumericVersion(5, 0, 4));
    assertThat(new File(folder.getRoot(), "fileToUpdate").exists(), is(true));
  }

  @AfterClass
  public static void tearDown() throws Exception {
    fileServer.stop();
  }

  private HttpInstaller createInstaller(Installation installation) throws MalformedURLException {
    URL baseUrl = new URL("http://localhost:8080/updates/");
    NullReport report = new NullReport();
    return new HttpInstaller(report, baseUrl, installation);
  }
}
