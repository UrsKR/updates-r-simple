package de.idos.updates.integrationtest;

import cucumber.annotation.After;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;
import cucumber.runtime.PendingException;
import de.idos.updates.FilesystemRepository;
import de.idos.updates.NumericVersion;
import de.idos.updates.UpdateSystem;
import de.idos.updates.Version;
import de.idos.updates.configuration.ConfiguredUpdateSystemFactory;
import de.idos.updates.configuration.RepositoryType;

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
    private File classPathConfig;
    private File workingDirConfig;
    private UpdateSystem updateSystem;

    @Given("^a file called 'update.properties' on the classpath$")
    public void a_file_called_update_properties_on_the_classpath() throws Throwable {
        Properties properties = createBaseProperties();
        properties.put("update.LatestVersion.repository.location", Repo_From_Classpath);
        classPathConfig = new File("./src/test/resources/update.properties");
        writeProperties(properties, classPathConfig);
        File repository = new File(Repo_From_Classpath, FilesystemRepository.AVAILABLE_VERSIONS);
        repository.mkdirs();
        new File(repository, "1.0.1").mkdir();
    }

    @Given("^a file called 'update.properties' in the working directory$")
    public void a_file_called_update_properties_in_the_working_directory() throws Throwable {
        Properties properties = createBaseProperties();
        properties.put("update.LatestVersion.repository.location", Repo_From_WorkingDir);
        workingDirConfig = new File("./update.properties");
        writeProperties(properties, workingDirConfig);
        File repository = new File(Repo_From_WorkingDir, FilesystemRepository.AVAILABLE_VERSIONS);
        repository.mkdirs();
        new File(repository, "1.0.2").mkdir();
    }

    @When("^the file specifies a fixed version to be loaded$")
    public void the_file_specifies_a_fixed_version_to_be_loaded() throws Throwable {
        Properties properties = new Properties();
        properties.load(new FileInputStream(classPathConfig));
        properties.put("update.strategy", "FixedVersion");
        properties.put("update.FixedVersion.location", Fixed_Version_Location);
        properties.store(new FileOutputStream(classPathConfig), "");
    }

    @When("^I start the update system$")
    public void I_start_the_update_system() throws Throwable {
        updateSystem = new ConfiguredUpdateSystemFactory().create();
    }

    @Then("^the system uses the file on the classpath to configure itself$")
    public void the_system_uses_the_file_on_the_classpath_to_configure_itself() throws Throwable {
        Version latestVersion = updateSystem.getLatestVersion();
        assertThat(latestVersion, is(sameVersionAs(new NumericVersion(1, 0, 1))));
    }

    @Then("^the system uses the file in the working directory to configure itself$")
    public void the_system_uses_the_file_in_the_working_directory_to_configure_itself() throws Throwable {
        Version latestVersion = updateSystem.getLatestVersion();
        assertThat(latestVersion, is(sameVersionAs(new NumericVersion(1, 0, 2))));
    }

    @Then("^the system points to that version to load$")
    public void the_system_points_to_that_version_to_load() throws Throwable {
        assertThat(updateSystem.getFolderForLatestVersion(), is(new File("./src/main/resources/FixedVersion")));
    }

    @After
    public void tearDown() throws IOException {
        deleteQuietly(workingDirConfig);
        deleteQuietly(new File(Repo_From_WorkingDir));
        deleteQuietly(new File(Repo_From_Classpath));
        deleteQuietly(new File(Fixed_Version_Location));
    }

    private Properties createBaseProperties() {
        Properties properties = new Properties();
        properties.put("update.applicationName", "integrationtest");
        properties.put("update.strategy", "LatestVersion");
        properties.put("update.LatestVersion.repository.type", RepositoryType.File.toString());
        return properties;
    }

    private void writeProperties(Properties properties, File file) throws IOException {
        FileOutputStream stream = new FileOutputStream(file);
        properties.store(stream, "");
        stream.close();
    }
}
