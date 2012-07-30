package de.idos.updates.configuration;

import de.idos.updates.FilesystemRepository;
import de.idos.updates.NumericVersion;
import de.idos.updates.UpdateAvailability;
import de.idos.updates.UpdateSystem;
import de.idos.updates.Updater;
import de.idos.updates.Version;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import static de.idos.updates.NumericVersionMatchers.sameVersionAs;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConfiguredUpdateSystemFactory_FixedVersionTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();
  private final Properties properties = new Properties();
  private File configuration;
  private File fixedVersionFolder;

  @Before
  public void configureFileRepository() throws Exception {
    File repository = folder.newFolder("repository");
    File available_versions = new File(repository, FilesystemRepository.AVAILABLE_VERSIONS);
    new File(available_versions, "4.2.1").mkdirs();
    fixedVersionFolder = folder.newFile("fixedVersion");
    configuration = new File(".", "update.properties");
    properties.put("update.applicationName", "updateunittest");
    properties.put("update.strategy", "FixedVersion");
    properties.put("update.FixedVersion.location", fixedVersionFolder.getAbsolutePath());
    properties.put("update.LatestVersion.repository.type", "File");
    properties.put("update.LatestVersion.repository.location", repository.getAbsolutePath());
    properties.store(new FileOutputStream(configuration), "");
  }

  @Test
  public void statesConfiguredFolderAsVersionFolder() throws Exception {
    UpdateSystem updateSystem = ConfiguredUpdateSystem.loadProperties().create();
    File folder = updateSystem.getFolderForVersionToRun();
    assertThat(folder, is(fixedVersionFolder));
  }

  @Test
  public void canInstallUpdatesEvenWhenTheActualVersionIsFixed() throws Exception {
    UpdateSystem updateSystem = ConfiguredUpdateSystem.loadProperties().create();
    Updater updater = getUpdaterThatHasRun(updateSystem);
    updater.updateToLatestVersion();
    assertThat(getUpdaterThatHasRun(updateSystem).hasUpdate(), is(UpdateAvailability.NotAvailable));
  }

  @Test
  public void canSetFixedVersionNumber() throws Exception {
    NumericVersion overriddenVersion = new NumericVersion(3, 3, 1);
    UpdateSystem updateSystem = ConfiguredUpdateSystem.loadProperties().andIfTheVersionIsFixedSetItTo(overriddenVersion).create();
    Version installedVersion = getUpdaterThatHasRun(updateSystem).getInstalledVersion();
    assertThat(installedVersion, is(sameVersionAs(overriddenVersion)));
  }

  @After
  public void deleteConfiguration() throws Exception {
    FileUtils.deleteQuietly(configuration);
  }

  @After
  public void deleteInstalledUpdates() throws Exception {
    String userHome = System.getProperty("user.home");
    FileUtils.deleteQuietly(new File(userHome, ".updateunittest"));
  }

  private Updater getUpdaterThatHasRun(UpdateSystem updateSystem) {
    Updater updater = updateSystem.checkForUpdates();
    updater.runCheck();
    return updater;
  }
}