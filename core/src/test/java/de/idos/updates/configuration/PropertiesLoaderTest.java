package de.idos.updates.configuration;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PropertiesLoaderTest {

    File fileInWorkingDirectory = new File(".", "unittestcombination.properties");

    @Test
    public void loadsPropertiesFromClassPath() throws Exception {
        Properties properties = new PropertiesLoader().load("unittestclasspath.properties");
        assertThat(properties.getProperty("X"), is("Y"));
    }

    @Test
    public void prefersPropertiesFromFileSystem() throws Exception {
        FileUtils.writeStringToFile(fileInWorkingDirectory, "X=X");
        Properties properties = new PropertiesLoader().load("unittestcombination.properties");
        assertThat(properties.getProperty("X"), is("X"));
    }

    @Test
    public void addsPropertiesTogether() throws Exception {
        FileUtils.writeStringToFile(fileInWorkingDirectory, "X=X");
        Properties properties = new PropertiesLoader().load("unittestcombination.properties");
        assertThat(properties.getProperty("A"), is("B"));
    }

    @Test
    public void closesStreamOnFileSystemFile() throws Exception {
        fileInWorkingDirectory.createNewFile();
        new PropertiesLoader().load("unittestcombination.properties");
        fileInWorkingDirectory.delete();
        assertThat(fileInWorkingDirectory.exists(), is(false));
    }

    @Test(expected = ConfigurationFailedException.class)
    public void throwsExceptionIfNeitherFileExists() throws Exception {
        new PropertiesLoader().load("missing.properties");
    }

    @After
    public void deleteConfigurationFiles() throws Exception {
        fileInWorkingDirectory.delete();
    }
}