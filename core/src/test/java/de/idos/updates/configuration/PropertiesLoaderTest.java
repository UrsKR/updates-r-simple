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
        Properties properties = new PropertiesLoader("unittestclasspath.properties").load();
        assertThat(properties.getProperty("X"), is("Y"));
    }

    @Test
    public void prefersPropertiesFromFileSystem() throws Exception {
        FileUtils.writeStringToFile(fileInWorkingDirectory, "X=X");
        Properties properties = new PropertiesLoader("unittestcombination.properties").load();
        assertThat(properties.getProperty("X"), is("X"));
    }

    @Test
    public void addsPropertiesTogether() throws Exception {
        FileUtils.writeStringToFile(fileInWorkingDirectory, "X=X");
        Properties properties = new PropertiesLoader("unittestcombination.properties").load();
        assertThat(properties.getProperty("A"), is("B"));
    }

    @Test
    public void closesStreamOnFileSystemFile() throws Exception {
        fileInWorkingDirectory.createNewFile();
        new PropertiesLoader("unittestcombination.properties").load();
        fileInWorkingDirectory.delete();
        assertThat(fileInWorkingDirectory.exists(), is(false));
    }

    @Test(expected = ConfigurationFailedException.class)
    public void throwsExceptionIfNeitherFileExists() throws Exception {
        new PropertiesLoader("missing.properties").load();
    }

    @After
    public void deleteConfigurationFiles() throws Exception {
        fileInWorkingDirectory.delete();
    }
}