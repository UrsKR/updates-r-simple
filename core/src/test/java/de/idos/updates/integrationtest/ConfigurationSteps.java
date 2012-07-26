package de.idos.updates.integrationtest;

import cucumber.annotation.After;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;
import de.idos.updates.*;
import de.idos.updates.configuration.Configurator;
import de.idos.updates.configuration.ConfiguredUpdateSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static de.idos.updates.NumericVersionMatchers.sameVersionAs;
import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("UnusedDeclaration")
public class ConfigurationSteps {
    public static final String Repo_From_WorkingDir = "./src/main/resources/repository_from_workingdir";
    public static final String Repo_From_Classpath = "./src/main/resources/repository_from_classpath";
    public static final String Fixed_Version_Location = "./src/main/resources/FixedVersion";
    private File classPathConfig = new File("./src/test/resources/update.properties");
    private File workingDirConfig = new File("./update.properties");
    private UpdateSystem updateSystem;

    @Given("^a file called 'update.properties' on the classpath$")
    public void a_file_called_update_properties_on_the_classpath() throws Throwable {
        deleteQuietly(workingDirConfig);
        File repository = new File(Repo_From_Classpath, FilesystemRepository.AVAILABLE_VERSIONS);
        repository.mkdirs();
        new File(repository, "1.0.1").mkdir();
    }

    @Given("^a file called 'update.properties' in the working directory$")
    public void a_file_called_update_properties_in_the_working_directory() throws Throwable {
        Configurator configurator = createBaseProperties();
        configurator.setRepositoryLocationForLatestVersionTo(Repo_From_WorkingDir);
        configurator.saveConfiguration();
        File repository = new File(Repo_From_WorkingDir, FilesystemRepository.AVAILABLE_VERSIONS);
        repository.mkdirs();
        new File(repository, "1.0.2").mkdir();
    }

    @Given("^the file specifies a fixed version to be loaded$")
    public void the_file_specifies_a_fixed_version_to_be_loaded() throws Throwable {
        Properties properties = new Properties();
        properties.load(new FileInputStream(workingDirConfig));
        Configurator configurator = new Configurator(properties);
        configurator.toggleFixedVersion();
        configurator.changeFixedVersionLocationTo(Fixed_Version_Location);
        configurator.saveConfiguration();
    }

    @When("^I start the update system$")
    public void I_start_the_update_system() throws Throwable {
        updateSystem = ConfiguredUpdateSystem.loadProperties().create();
    }

    @Then("^the system uses the file on the classpath to configure itself$")
    public void the_system_uses_the_file_on_the_classpath_to_configure_itself() throws Throwable {
        Version latestVersion = updateSystem.checkForUpdates().getLatestVersion();
        assertThat(latestVersion, is(sameVersionAs(new NumericVersion(1, 0, 1))));
    }

    @Then("^the system uses the file in the working directory to configure itself$")
    public void the_system_uses_the_file_in_the_working_directory_to_configure_itself() throws Throwable {
        Version latestVersion = updateSystem.checkForUpdates().getLatestVersion();
        assertThat(latestVersion, is(sameVersionAs(new NumericVersion(1, 0, 2))));
    }

    @Then("^the system points to that version to load$")
    public void the_system_points_to_that_version_to_load() throws Throwable {
        assertThat(updateSystem.getFolderForVersionToRun(), is(new File("./src/main/resources/FixedVersion")));
    }

    @After
    public void tearDown() throws IOException {
        deleteQuietly(workingDirConfig);
        deleteQuietly(new File(Repo_From_WorkingDir));
        deleteQuietly(new File(Repo_From_Classpath));
        deleteQuietly(new File(Fixed_Version_Location));
    }

    private Configurator createBaseProperties() {
        Configurator configurator = new Configurator();
        configurator.setApplicationNameTo("integrationtest");
        configurator.toggleLatestVersion();
        configurator.toggleFileRepositoryForLatestVersion();
        return configurator;
    }

    private void writeProperties(Properties properties, File file) throws IOException {
        FileOutputStream stream = new FileOutputStream(file);
        properties.store(stream, "");
        stream.close();
    }
}
