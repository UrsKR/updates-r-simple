package de.idos.updates.integrationtest;

import cucumber.annotation.After;
import cucumber.annotation.Before;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;
import de.idos.updates.DefaultUpdateSystem;
import de.idos.updates.NumericVersion;
import de.idos.updates.UpdateAvailability;
import de.idos.updates.Updater;
import de.idos.updates.Version;
import de.idos.updates.VersionStore;
import de.idos.updates.repository.FilesystemRepository;
import de.idos.updates.repository.HttpRepository;
import de.idos.updates.repository.Repository;
import de.idos.updates.server.FileServer;
import de.idos.updates.store.FileDataInVersion;
import de.idos.updates.store.FilesystemInstallationStarter;
import de.idos.updates.store.FilesystemVersionStore;
import de.idos.updates.store.Installation;
import de.idos.updates.store.OngoingInstallation;
import de.idos.updates.store.ProgressReport;
import de.idos.updates.store.ZipFileMother;
import de.idos.updates.store.ZipInstallationStarter;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import static de.idos.updates.NumericVersionMatchers.sameVersionAs;
import static de.idos.updates.repository.FilesystemRepository.AVAILABLE_VERSIONS;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SuppressWarnings("UnusedDeclaration")
public class UpdatesSteps {
  private static final NumericVersion currentVersion = new NumericVersion(4, 2, 0);
  public static final String DESCRIPTION_FOR_NEW_VERSION = "Description for 4.2.1";
  private TemporaryFolder folder = new TemporaryFolder();
  private DefaultUpdateSystem updateSystem;
  private NumericVersion latestVersion;
  private File repositoryFolder;
  private File versionStoreFolder;
  private final UpdateSystemBuilder updateSystemBuilder = new UpdateSystemBuilder();
  private final ProgressReport verifiableReport = mock(ProgressReport.class);
  private VersionStore versionStore;
  private CountDownLatch slowThreadStartSignal;
  private OngoingInstallation ongoingInstallation = null;
  private final FileServer fileServer = new FileServer();
  private Updater updater;

  @Before
  public void initializeVersionRepository() throws Throwable {
    folder.create();
    this.repositoryFolder = folder.newFolder("repository");
    this.versionStoreFolder = folder.newFolder("versions");
    Repository repository = new FilesystemRepository(repositoryFolder);
    versionStore = new FilesystemVersionStore(versionStoreFolder,
            new ZipInstallationStarter(new FilesystemInstallationStarter()));
    updateSystemBuilder.useStore(versionStore);
    updateSystemBuilder.useRepository(repository);
    Installation installation = versionStore.beginInstallation(currentVersion);
    File file = folder.newFile();
    installation.addContent(new FileDataInVersion(file));
    installation.finish();
  }

  @Given("^the repository contains a new version$")
  public void the_repository_contains_a_more_recent_version() throws Throwable {
    File versionsFolder = new File(repositoryFolder, AVAILABLE_VERSIONS);
    File latestVersionFolder = new File(versionsFolder, "4.2.1");
    latestVersionFolder.mkdir();
    new File(latestVersionFolder, "content").createNewFile();
    this.latestVersion = new NumericVersion(4, 2, 1);
  }

  @Given("^the repository contains several new versions$")
  public void the_repository_contains_several_new_versions() throws Throwable {
    File versionsFolder = new File(repositoryFolder, AVAILABLE_VERSIONS);
    new File(versionsFolder, "4.2.1").mkdir();
    new File(versionsFolder, "4.2.2").mkdir();
    this.latestVersion = new NumericVersion(4, 2, 2);
  }

  @Given("^the repository does not contain a new version$")
  public void the_repository_does_not_contain_a_new_version() throws Throwable {
    this.latestVersion = currentVersion;
  }

  @Given("^the repository contains a new version packed as zip$")
  public void the_repository_contains_a_new_version_packed_as_zip() throws Throwable {
    File versionsFolder = new File(repositoryFolder, AVAILABLE_VERSIONS);
    File latestVersionFolder = new File(versionsFolder, "4.2.1");
    latestVersionFolder.mkdir();
    File staging = folder.newFolder("staging");
    File content = ZipFileMother.createContentFileForZip(staging, "contentFromZip");
    ZipFileMother.createZipFileInTemporaryFolder(latestVersionFolder, "v4.2.1.zip", content);
    this.latestVersion = new NumericVersion(4, 2, 1);
  }

  @Given("^the application was updated$")
  public void the_application_was_updated() throws Throwable {
    File oldVersionFolder = new File(versionStoreFolder, "4.1.2");
    File newVersionFolder = new File(versionStoreFolder, "4.2.0");
    oldVersionFolder.mkdir();
    newVersionFolder.mkdir();
  }

  @Given("^an HTTP-server with new versions$")
  public void an_HTTP_Server_with_new_versions() throws Throwable {
    fileServer.start();
  }

  @Given("^a repository for that server$")
  public void an_HTTP_Repository_for_that_server() throws Throwable {
    Repository repository = new HttpRepository("http://localhost:8080/updates/");
    updateSystemBuilder.useRepository(repository);
  }

  @Given("^I have registered a client to receive status updates$")
  public void I_have_registered_a_client_to_receive_status_updates() throws Throwable {
    updateSystemBuilder.addReporter(verifiableReport);
  }

  @When("^the application checks for updates$")
  public void the_application_checks_for_updates() throws Throwable {
    this.updater = getUpdateSystem().checkForUpdates();
    this.updater.runCheck();
  }

  @When("^the application requests an update$")
  public void the_application_requests_an_update() throws Throwable {
    the_application_checks_for_updates();
    ongoingInstallation = updater.updateToLatestVersion();
  }

  @When("^I instruct the library to clean up$")
  public void I_instruct_the_library_to_clean_up() throws Throwable {
    getUpdateSystem().removeOldVersions();
  }

  @When("^the application requests an update that takes a while to complete$")
  public void the_application_requests_an_update_that_takes_a_while_to_complete() throws Throwable {
    this.slowThreadStartSignal = new CountDownLatch(1);
    new Thread(new Runnable() {
      @Override
      public void run() {
        updateSystemBuilder.useStore(new SlowVersionStore(versionStore, slowThreadStartSignal));
        DefaultUpdateSystem slowUpdateSystem = updateSystemBuilder.create();
        Updater updater = slowUpdateSystem.checkForUpdates();
        updater.runCheck();
        ongoingInstallation = updater.updateToLatestVersion();
      }
    }).start();
  }

  @When("^the application concurrently request a second update$")
  public void the_application_concurrently_request_a_second_update() throws Throwable {
    I_have_registered_a_client_to_receive_status_updates();
    updateSystemBuilder.useStore(versionStore);
    the_application_requests_an_update();
    slowThreadStartSignal.countDown();
  }

  @When("^the application asks the library to abort the update$")
  public void the_application_asks_the_library_to_abort_the_update() throws Throwable {
    while (ongoingInstallation == null) {
      Thread.sleep(500);
    }
    ongoingInstallation.abort();
  }

  @Then("^no trace of the update remains$")
  public void no_trace_of_the_update_remains() throws Throwable {
    assertThat(new File(versionStoreFolder, "4.2.1").exists(), is(false));
  }

  @Then("^the library reports an update$")
  public void the_library_reports_an_update() throws Throwable {
    assertThat(updater.hasUpdate(), is(UpdateAvailability.Available));
  }

  @Then("^the library reports the new version$")
  public void the_library_reports_the_new_version() throws Throwable {
    assertLatestReportedVersionIsLatestExpectedVersion();
  }

  @Then("^the library reports the most recent version$")
  public void the_library_reports_the_most_recent_version() throws Throwable {
    assertLatestReportedVersionIsLatestExpectedVersion();
  }

  @Then("^the library does not indicate a new version$")
  public void the_library_does_not_indicate_a_new_version() throws Throwable {
    assertThat(updater.hasUpdate(), is(UpdateAvailability.NotAvailable));
  }

  @Then("^the library reports the current version as latest$")
  public void the_library_reports_the_current_version_as_latest() throws Throwable {
    assertLatestReportedVersionIsLatestExpectedVersion();
  }

  @Then("^the library downloads and stores the required files$")
  public void the_library_downloads_and_stores_the_required_files() throws Throwable {
    while (ongoingInstallation.isRunning()) {
      Thread.sleep(500);
    }
    File versionFolder = new File(versionStoreFolder, "4.2.1");
    assertThat(new File(versionFolder, "content").exists(), is(true));
  }

  @Then("^the library downloads the zip and stores its content$")
  public void the_library_downloads_the_zip_and_stores_the_content() throws Throwable {
    while (ongoingInstallation.isRunning()) {
      Thread.sleep(500);
    }
    File versionFolder = new File(versionStoreFolder, "4.2.1");
    assertThat(new File(versionFolder, "contentFromZip").exists(), is(true));
  }

  @Then("^the library deletes all version but current one$")
  public void the_library_deletes_all_version_but_current_one() throws Throwable {
    assertThat(new File(versionStoreFolder, "4.1.2").exists(), is(false));
    assertThat(new File(versionStoreFolder, "4.2.0").exists(), is(true));
  }

  @Then("^the library reports the download's progress to the client$")
  public void the_library_reports_the_download_s_progress_to_the_client() throws Throwable {
    while (ongoingInstallation.isRunning()) {
      Thread.sleep(500);
    }
    verify(verifiableReport, atLeastOnce()).expectedSize(anyInt());
  }

  @Then("^the second update does not interfere$")
  public void the_second_update_does_not_interfere() throws Throwable {
    verify(verifiableReport).updateAlreadyInProgress();
  }

  @After
  public void deleteTemporaryFolder() {
    this.folder.delete();
  }

  @After
  public void stopServer() throws Exception {
    fileServer.stop();
  }

  private void assertLatestReportedVersionIsLatestExpectedVersion() {
    Version latestReportedVersion = updater.getLatestVersion();
    assertThat(latestReportedVersion, is(sameVersionAs(latestVersion)));
  }

  private DefaultUpdateSystem getUpdateSystem() {
    if (updateSystem == null) {
      this.updateSystem = updateSystemBuilder.create();
    }
    return updateSystem;
  }
}