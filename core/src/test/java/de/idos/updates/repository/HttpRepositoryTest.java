package de.idos.updates.repository;

import de.idos.updates.NumericVersion;
import de.idos.updates.Version;
import de.idos.updates.install.InstallationStrategy;
import de.idos.updates.server.FileServer;
import de.idos.updates.store.FilesystemInstallationStarter;
import de.idos.updates.store.FilesystemVersionStore;
import de.idos.updates.store.Installation;
import de.idos.updates.store.UrlDataInVersion;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static de.idos.updates.NumericVersionMatchers.sameVersionAs;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HttpRepositoryTest {
  private static FileServer fileServer;

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  HttpRepository repository = new HttpRepository("http://localhost:8080/");

  @BeforeClass
  public static void setUp() throws Exception {
    fileServer = new FileServer();
    fileServer.start();
  }

  @AfterClass
  public static void tearDown() throws Exception {
    fileServer.stop();
  }

  @Test
  public void retrievesLatestVersionFromServer() throws Exception {
    Version latestVersion = repository.getLatestVersion();
    assertThat(latestVersion, is(sameVersionAs(new NumericVersion(5, 0, 4))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void doesNotAcceptBadUrls() throws Exception {
    new HttpRepository("xx");
  }

  @Test
  public void createsHttpInstaller() throws Exception {
    Installation installation = mock(Installation.class);
    InstallationStrategy<String> strategy = repository.createInstallationStrategy(installation);
    Version mock = mock(Version.class);
    strategy.installElement("X", mock);
    verify(installation).addContent(isA(UrlDataInVersion.class));
  }

  @Test
  public void worksIfNoReporterIsRegistered() throws Exception {
    repository.transferVersionTo(new NumericVersion(5, 0, 4), new FilesystemVersionStore(folder.getRoot(), new FilesystemInstallationStarter()));
    assertThat(new File(folder.getRoot(), "5.0.4").exists(), is(true));
  }
}