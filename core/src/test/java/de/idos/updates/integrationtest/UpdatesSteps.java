package de.idos.updates.integrationtest;

import cucumber.annotation.After;
import cucumber.annotation.Before;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;
import de.idos.updates.*;
import de.idos.updates.server.FileServer;
import de.idos.updates.store.FileDataInVersion;
import de.idos.updates.store.FilesystemVersionStore;
import de.idos.updates.VersionStore;
import de.idos.updates.store.ProgressReport;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static de.idos.updates.FilesystemRepository.AVAILABLE_VERSIONS;
import static de.idos.updates.NumericVersionMatchers.sameVersionAs;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SuppressWarnings("UnusedDeclaration")
public class UpdatesSteps {

    private static final NumericVersion currentVersion = new NumericVersion(4, 2, 0);
    private TemporaryFolder folder = new TemporaryFolder();
    private DefaultUpdateSystem updateSystem;
    private NumericVersion latestVersion;
    private File repositoryFolder;
    private File versionStoreFolder;
    private final UpdateSystemBuilder updateSystemBuilder = new UpdateSystemBuilder();
    private final ProgressReport verifiableReport = mock(ProgressReport.class);

    @Before
    public void initializeVersionRepository() throws Throwable {
        folder.create();
        this.repositoryFolder = folder.newFolder("repository");
        this.versionStoreFolder = folder.newFolder("versions");
        Repository repository = new FilesystemRepository(repositoryFolder);
        VersionStore versionStore = new FilesystemVersionStore(versionStoreFolder);
        updateSystemBuilder.useStore(versionStore);
        updateSystemBuilder.useRepository(repository);
        versionStore.addVersion(currentVersion);
        File file = folder.newFile();
        versionStore.addContent(currentVersion, new FileDataInVersion(file));
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

    @Given("^the application was updated$")
    public void the_application_was_updated() throws Throwable {
        File oldVersionFolder = new File(versionStoreFolder, "4.1.2");
        File newVersionFolder = new File(versionStoreFolder, "4.2.0");
        oldVersionFolder.mkdir();
        newVersionFolder.mkdir();
    }

    @Given("^an HTTP-server with new versions$")
    public void an_HTTP_Server_with_new_versions() throws Throwable {
        new FileServer().start();
    }

    @Given("^a repository for that server$")
    public void an_HTTP_Repository_for_that_server() throws Throwable {
        Repository repository = new HttpRepository("http://localhost:8080/updates");
        updateSystemBuilder.useRepository(repository);
    }

    @Given("^I have registered a client to receive status updates$")
    public void I_have_registered_a_client_to_receive_status_updates() throws Throwable {
        updateSystemBuilder.addReporter(verifiableReport);
    }

    @When("^the application checks for updates$")
    public void the_application_checks_for_updates() throws Throwable {
        getUpdateSystem().checkForUpdates();
    }

    @When("^the application requests an update$")
    public void the_application_requests_an_update() throws Throwable {
        getUpdateSystem().checkForUpdates().updateToLatestVersion();
    }

    @When("^I instruct the library to clean up$")
    public void I_instruct_the_library_to_clean_up() throws Throwable {
        getUpdateSystem().removeOldVersions();
    }

    @Then("^the library reports an update$")
    public void the_library_reports_an_update() throws Throwable {
        assertThat(getUpdateSystem().checkForUpdates().hasUpdate(), is(UpdateAvailability.Available));
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
        assertThat(getUpdateSystem().checkForUpdates().hasUpdate(), is(UpdateAvailability.NotAvailable));
    }

    @Then("^the library reports the current version as latest$")
    public void the_library_reports_the_current_version_as_latest() throws Throwable {
        assertLatestReportedVersionIsLatestExpectedVersion();
    }

    @Then("^the library downloads and stores the required files$")
    public void the_library_downloads_and_stores_the_required_files() throws Throwable {
        File versionFolder = new File(versionStoreFolder, "4.2.1");
        assertThat(new File(versionFolder, "content").exists(), is(true));
    }

    @Then("^the library deletes all version but current one$")
    public void the_library_deletes_all_version_but_current_one() throws Throwable {
        assertThat(new File(versionStoreFolder, "4.1.2").exists(), is(false));
        assertThat(new File(versionStoreFolder, "4.2.0").exists(), is(true));
    }

    @Then("^the library reports the download's progress to the client$")
    public void the_library_reports_the_download_s_progress_to_the_client() throws Throwable {
        verify(verifiableReport).expectedSize(anyInt());
    }

    @After
    public void deleteTemporaryFolder() {
        this.folder.delete();
    }

    private void assertLatestReportedVersionIsLatestExpectedVersion() {
        Version latestReportedVersion = getUpdateSystem().checkForUpdates().getLatestVersion();
        assertThat(latestReportedVersion, is(sameVersionAs(latestVersion)));
    }

    private DefaultUpdateSystem getUpdateSystem() {
        if (updateSystem == null) {
            this.updateSystem = updateSystemBuilder.create();
        }
        return updateSystem;
    }
}