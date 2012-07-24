package de.idos.updates.configuration;

import de.idos.updates.NumericVersion;
import de.idos.updates.UpdateSystem;
import de.idos.updates.Version;
import de.idos.updates.server.FileServer;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import static de.idos.updates.NumericVersionMatchers.sameVersionAs;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConfiguredUpdateSystemFactory_HttpTest {
    private FileServer fileServer;
    private File configuration;
    Properties properties = new Properties();

    @Before
    public void startServer() throws Exception {
        fileServer = new FileServer();
        fileServer.start();
    }

    @Before
    public void createConfiguration() throws Exception {
        configuration = new File(".", "update.properties");
        properties.put("update.applicationName", "updateunittest");
        properties.put("update.LatestVersion.repository.type", "HTTP");
        properties.put("update.LatestVersion.repository.location", "http://localhost:8080");
        properties.put("update.strategy", "LatestVersion");
        properties.store(new FileOutputStream(configuration), "");
    }

    @Test
    public void usesConfiguredHttpRepository() throws Exception {
        UpdateSystem updateSystem = new ConfiguredUpdateSystemFactory().create();
        Version latestVersion = updateSystem.checkForUpdates().getLatestVersion();
        assertThat(latestVersion, is(sameVersionAs(new NumericVersion(5, 0, 4))));
    }

    @Test
    public void storesInAppNameFolder() throws Exception {
        UpdateSystem updateSystem = new ConfiguredUpdateSystemFactory().create();
        updateSystem.checkForUpdates().updateToLatestVersion();
        String userHome = System.getProperty("user.home");
        assertThat(new File(userHome, ".updateunittest/versions").exists(), is(true));
    }

    @After
    public void stopServer() throws Exception {
        fileServer.stop();
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