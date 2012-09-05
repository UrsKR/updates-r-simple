package de.idos.updates.configuration;

import de.idos.updates.NumericVersion;
import de.idos.updates.UpdateAvailability;
import de.idos.updates.UpdateSystem;
import de.idos.updates.Updater;
import de.idos.updates.Version;
import de.idos.updates.repository.FilesystemRepository;
import de.idos.updates.store.OngoingInstallation;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static de.idos.updates.NumericVersionMatchers.sameVersionAs;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConfiguredUpdateSystem_FixedVersionTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();
  private File configuration = new File(".", "update.properties");
  private File fixedVersionFolder;

  @Before
  public void configureFileRepository() throws Exception {
    File repository = folder.newFolder("repository");
    File available_versions = new File(repository, FilesystemRepository.AVAILABLE_VERSIONS);
    new File(available_versions, "4.2.1").mkdirs();
    fixedVersionFolder = folder.newFile("fixedVersion");
    Configurator configurator = new Configurator();
    configurator.setApplicationNameTo("updateunittest");
    configurator.toggleFixedVersion();
    configurator.changeFixedVersionLocationTo(fixedVersionFolder.getAbsolutePath());
    configurator.toggleFileRepositoryForLatestVersion();
    configurator.setRepositoryLocationForLatestVersionTo(repository.getAbsolutePath());
    configurator.saveConfiguration();
  }

  @Test
  public void statesConfiguredFolderAsVersionFolder() throws Exception {
    UpdateSystem updateSystem = ConfiguredUpdateSystem.loadProperties().create();
    File folder = updateSystem.getFolderForVersionToRun();
    assertThat(folder, is(fixedVersionFolder));
  }

  @Test(timeout = 1500)
  public void canInstallUpdatesEvenWhenTheActualVersionIsFixed() throws Exception {
    UpdateSystem updateSystem = ConfiguredUpdateSystem.loadProperties().create();
    Updater updater = getUpdaterThatHasRun(updateSystem);
    OngoingInstallation installation = updater.updateToLatestVersion();
    while(installation.isRunning()){
      //wait
    }
    Thread.sleep(500);
    assertThat(getUpdaterThatHasRun(updateSystem).hasUpdate(), is(UpdateAvailability.NotAvailable));
  }

  @Test
  public void canOverrideUnknownFixedVersionNumber() throws Exception {
    NumericVersion overriddenVersion = new NumericVersion(3, 3, 1);
    UpdateSystem updateSystem = ConfiguredUpdateSystem.loadProperties().andIfTheInstalledVersionIsUnknownUse(overriddenVersion).create();
    Version installedVersion = getUpdaterThatHasRun(updateSystem).getInstalledVersion();
    assertThat(installedVersion, is(sameVersionAs(overriddenVersion)));
  }

  @Test
  public void returnsFixedVersionNumberEvenIfALaterVersionIsInstalled() throws Exception {
    File versionsFolder = new File(getVersionParent(), "versions");
    new File(versionsFolder, "4.0.0").mkdirs();
    NumericVersion overriddenVersion = new NumericVersion(3, 3, 1);
    UpdateSystem updateSystem = ConfiguredUpdateSystem.loadProperties().andIfTheInstalledVersionIsUnknownUse(overriddenVersion).create();
    Version installedVersion = getUpdaterThatHasRun(updateSystem).getInstalledVersion();
    assertThat(installedVersion, is(sameVersionAs(overriddenVersion)));
  }

  @After
  public void deleteConfiguration() throws Exception {
    FileUtils.deleteQuietly(configuration);
  }

  @After
  public void deleteInstalledUpdates() throws Exception {
    File versionStorageParent = getVersionParent();
    FileUtils.deleteQuietly(versionStorageParent);
  }

  private File getVersionParent() {
    String userHome = System.getProperty("user.home");
    return new File(userHome, ".updateunittest");
  }

  private Updater getUpdaterThatHasRun(UpdateSystem updateSystem) {
    Updater updater = updateSystem.checkForUpdates();
    updater.runCheck();
    return updater;
  }
}