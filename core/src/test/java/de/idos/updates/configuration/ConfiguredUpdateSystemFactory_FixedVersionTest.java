package de.idos.updates.configuration;

import de.idos.updates.FilesystemRepository;
import de.idos.updates.UpdateSystem;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConfiguredUpdateSystemFactory_FixedVersionTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private File configuration;
    Properties properties = new Properties();
    private File fixedVersionFolder;

    @Before
    public void fillRepository() throws Exception {
        File repository = folder.newFolder("repository");
        File available_versions = new File(repository, FilesystemRepository.AVAILABLE_VERSIONS);
        new File(available_versions, "4.2.1").mkdirs();
    }

    @Before
    public void configureFileRepository() throws Exception {
        fixedVersionFolder = folder.newFile("fixedVersion");
        configuration = new File(".", "update.properties");
        properties.put("update.applicationName", "updateunittest");
        properties.put("update.strategy", "FixedVersion");
        properties.put("update.FixedVersion.location", fixedVersionFolder.getAbsolutePath());
        properties.store(new FileOutputStream(configuration), "");
    }

    @Test
    public void statesConfiguredFolderAsVersionFolder() throws Exception {
        UpdateSystem updateSystem = new ConfiguredUpdateSystemFactory().create();
        File folder = updateSystem.getFolderForLatestVersion();
        assertThat(folder, is(fixedVersionFolder));
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