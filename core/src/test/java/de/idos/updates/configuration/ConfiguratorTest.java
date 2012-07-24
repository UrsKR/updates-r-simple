package de.idos.updates.configuration;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConfiguratorTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Properties properties = new Properties();
    private Configurator configurator = new Configurator(properties);
    private File propertiesFile = new File(".", "update.properties");

    @Test
    public void writesConfigurationToUpdatePropertiesInWorkingDirectory() throws Exception {
        configurator.saveConfiguration();
        assertThat(propertiesFile.exists(), is(true));
    }

    @Test
    public void writesOriginalProperties() throws Exception {
        properties.put("X", "Y");
        assertThatItWrites("X", "Y");
    }

    @Test
    public void closesStreamAfterWriting() throws Exception {
        configurator.saveConfiguration();
        propertiesFile.delete();
        assertThat(propertiesFile.exists(), is(false));
    }

    @Test
    public void writesApplicationName() throws Exception {
        configurator.setApplicationNameTo("MyApp");
        assertThatItWrites("update.applicationName", "MyApp");
    }

    @Test
    public void canSwitchToFixedVersion() throws Exception {
        configurator.toggleFixedVersion();
        assertThatItWrites("update.strategy", "FixedVersion");
    }

    @Test
    public void canSwitchToLatestVersion() throws Exception {
        configurator.toggleLatestVersion();
        assertThatItWrites("update.strategy", "LatestVersion");
    }

    @Test
    public void setsFixedVersionLocation() throws Exception {
        configurator.changeFixedVersionLocationTo("./lib");
        assertThatItWrites("update.FixedVersion.location", "./lib");
    }

    @Test
    public void canToggleHttpRepository() throws Exception {
        configurator.toggleHttpRepositoryForLatestVersion();
        assertThatItWrites("update.LatestVersion.repository.type", "HTTP");
    }

    @Test
    public void canToggleFileRepository() throws Exception {
        configurator.toggleFileRepositoryForLatestVersion();
        assertThatItWrites("update.LatestVersion.repository.type", "File");
    }

    @Test
    public void setsRepositoryLocation() throws Exception {
        configurator.setRepositoryLocationForLatestVersionTo("./repo");
        assertThatItWrites("update.LatestVersion.repository.location", "./repo");
    }

    @Test
    public void startsWithEmptyProperties() throws Exception {
        new Configurator().saveConfiguration();
        String content = FileUtils.readFileToString(new File(".", "update.properties"));
        assertThat(content, is(not(containsString("="))));
    }

    @After
    public void tearDown() throws Exception {
        propertiesFile.delete();
    }

    private void assertThatItWrites(String key, String value) throws IOException {
        configurator.saveConfiguration();
        Properties newProperties = new PropertiesLoader("update.properties").load();
        assertThat(newProperties.getProperty(key), is(value));
    }
}