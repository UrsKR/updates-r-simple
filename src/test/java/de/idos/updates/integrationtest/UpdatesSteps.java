package de.idos.updates.integrationtest;

import cucumber.annotation.After;
import cucumber.annotation.Before;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;
import cucumber.runtime.PendingException;
import de.idos.updates.FilesystemRepository;
import de.idos.updates.NumericVersion;
import de.idos.updates.Repository;
import de.idos.updates.UpdateSystem;
import de.idos.updates.Version;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static de.idos.updates.FilesystemRepository.AVAILABLE_VERSIONS;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UpdatesSteps {

    private TemporaryFolder folder = new TemporaryFolder();
    private Repository repository;
    private UpdateSystem updateSystem;

    @Before
    public void initializeTemporaryFolderForRepository() throws Throwable {
        folder.create();
        this.repository = new FilesystemRepository(folder.getRoot());
        this.updateSystem = new UpdateSystem(repository);
    }

    @Given("^the repository contains a new version$")
    public void the_repository_contains_a_more_recent_version() throws Throwable {
        File versionsFolder = folder.newFolder(AVAILABLE_VERSIONS);
        new File(versionsFolder, "4.2.1").createNewFile();
    }

    @Given("^the repository contains several new versions$")
    public void the_repository_contains_several_new_versions() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }

    @Given("^the repository does not contain a new version$")
    public void the_repository_does_not_contain_a_new_version() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }

    @Given("^the application was updated$")
    public void the_application_was_updated() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }

    @When("^the application checks for updates$")
    public void the_application_checks_for_updates() throws Throwable {
        updateSystem.checkForUpdatesSinceVersion(new NumericVersion(4, 2, 0));
    }

    @When("^the application requests an update$")
    public void the_application_requests_an_update() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }

    @When("^I instruct the library to clean up$")
    public void I_instruct_the_library_to_clean_up() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }

    @Then("^the library reports an update$")
    public void the_library_reports_an_update() throws Throwable {
        assertThat(updateSystem.hasUpdate(), is(true));
    }

    @Then("^the library reports the new version$")
    public void the_library_reports_the_new_version() throws Throwable {
        Version latest = updateSystem.getLatestVersion();
        Version expectedVersion = new NumericVersion(4, 2, 1);
        assertThat(latest.isEqualTo(expectedVersion), is(true));
    }

    @Then("^the library reports the most recent version$")
    public void the_library_reports_the_most_recent_version() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }

    @Then("^the library does not indicate a new version$")
    public void the_library_does_not_indicate_a_new_version() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }

    @Then("^the library downloads and stores the required files$")
    public void the_library_downloads_and_stores_the_required_files() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }

    @Then("^the library does not tamper with the current version$")
    public void the_library_does_not_tamper_with_the_current_version() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }

    @Then("^the library deletes all version but current one$")
    public void the_library_deletes_all_version_but_current_one() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }

    @After
    public void deleteTemporaryFolder() {
        this.folder.delete();
    }
}