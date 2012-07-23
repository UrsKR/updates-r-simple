package de.idos.updates.configuration;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PropertiesLoaderTest {

    File fileInWorkingDirectory;
    File fileOnClasspath;

    @Before
    public void createFileOnClasspath() throws Exception {
        fileOnClasspath = new File("./src/test/resources/test.properties");
    }

    @Before
    public void createFileInWorkingDirectory() throws Exception {
        fileInWorkingDirectory = new File(".", "test.properties");
    }

    @Test
    public void loadsPropertiesFromClassPath() throws Exception {
        FileUtils.writeStringToFile(fileInWorkingDirectory, "X=Y");
        Properties properties = new PropertiesLoader().load("test.properties");
        assertThat(properties.getProperty("X"), is("Y"));
    }

    @Test
    public void prefersPropertiesFromFileSystem() throws Exception {
        FileUtils.writeStringToFile(fileOnClasspath, "X=Y");
        FileUtils.writeStringToFile(fileInWorkingDirectory, "X=X");
        Properties properties = new PropertiesLoader().load("test.properties");
        assertThat(properties.getProperty("X"), is("X"));
    }

    @Test
    public void closesStreamOnFileSystemFile() throws Exception {
        fileInWorkingDirectory.createNewFile();
        new PropertiesLoader().load("test.properties");
        fileInWorkingDirectory.delete();
        assertThat(fileInWorkingDirectory.exists(), is(false));
    }

    @Test(expected = ConfigurationFailedException.class)
    public void throwsExceptionIfNeitherFileExists() throws Exception {
        deleteConfigurationFiles();
        new PropertiesLoader().load("test.properties");
    }

    @After
    public void deleteConfigurationFiles() throws Exception {
        fileInWorkingDirectory.delete();
        fileOnClasspath.delete();
    }
}