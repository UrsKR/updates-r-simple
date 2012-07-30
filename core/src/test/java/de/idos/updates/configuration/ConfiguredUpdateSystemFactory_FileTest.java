package de.idos.updates.configuration;

import de.idos.updates.NumericVersion;
import de.idos.updates.UpdateSystem;
import de.idos.updates.Updater;
import de.idos.updates.Version;
import de.idos.updates.VersionDiscovery;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfiguredUpdateSystemFactory_FileTest {
  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Before
  public void fillRepository() throws Exception {
    File available_versions = folder.newFolder("available_versions");
    new File(available_versions, "4.2.1").mkdirs();
  }

  @Before
  public void createConfiguration() throws Exception {
    Configurator configurator = new Configurator();
    configurator.setApplicationNameTo("updateunittest");
    configurator.toggleLatestVersion();
    configurator.toggleFileRepositoryForLatestVersion();
    configurator.setRepositoryLocationForLatestVersionTo(folder.getRoot().getAbsolutePath());
    configurator.saveConfiguration();
  }

  @Test
  public void usesConfiguredFileRepository() throws Exception {
    UpdateSystem updateSystem = ConfiguredUpdateSystem.loadProperties().create();
    Version latestVersion = getUpdaterThatHasRun(updateSystem).getLatestVersion();
    assertThat(latestVersion, is(sameVersionAs(new NumericVersion(4, 2, 1))));
  }

  @Test
  public void canChangeDiscovery() throws Exception {
    VersionDiscovery discovery = mock(VersionDiscovery.class);
    NumericVersion overriddenVersion = new NumericVersion(4, 2, 2);
    when(discovery.getLatestVersion()).thenReturn(overriddenVersion);
    UpdateSystem updateSystem = ConfiguredUpdateSystem.loadProperties().butDiscoverAvailableVersionThrough(discovery).create();
    Updater updater = getUpdaterThatHasRun(updateSystem);
    Version latestVersion = updater.getLatestVersion();
    assertThat(latestVersion, is(sameVersionAs(overriddenVersion)));
  }

  @Test
  public void canOverrideUnknownInstalledVersion() throws Exception {
    NumericVersion overriddenVersion = new NumericVersion(4, 2, 2);
    UpdateSystem updateSystem = ConfiguredUpdateSystem.loadProperties().andIfTheInstalledVersionIsUnknownUse(overriddenVersion).create();
    Updater updater = getUpdaterThatHasRun(updateSystem);
    Version installedVersion = updater.getInstalledVersion();
    assertThat(installedVersion, is(sameVersionAs(overriddenVersion)));
  }

  @After
  public void deleteConfiguration() throws Exception {
    File configurationFile = new File(".", "update.properties");
    FileUtils.deleteQuietly(configurationFile);
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