package de.idos.updates.configuration;

import de.idos.updates.NumericVersion;
import de.idos.updates.UpdateSystem;
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

public class ConfiguredUpdateSystemFactory_FileTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private File configuration;
    Properties properties = new Properties();

    @Before
    public void fillRepository() throws Exception {
        File available_versions = folder.newFolder("available_versions");
        new File(available_versions, "4.2.1").mkdirs();
    }

    @Before
    public void createConfiguration() throws Exception {
        configuration = new File(".", "update.properties");
        properties.put("update.applicationName", "updateunittest");
        properties.put("update.LatestVersion.repository.type", "File");
        properties.put("update.LatestVersion.repository.location", folder.getRoot().getAbsolutePath());
        properties.put("update.strategy", "LatestVersion");
        properties.store(new FileOutputStream(configuration), "");
    }

    @Test
    public void usesConfiguredFileRepository() throws Exception {
        UpdateSystem updateSystem = new ConfiguredUpdateSystemFactory().create();
        Version latestVersion = updateSystem.getLatestVersion();
        assertThat(latestVersion, is(sameVersionAs(new NumericVersion(4, 2, 1))));
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
}