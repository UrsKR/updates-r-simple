package de.idos.updates.configuration;

import org.junit.Test;

import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UpdateConfigurationTest {
    Properties properties = new Properties();
    UpdateConfiguration configuration = new UpdateConfiguration(properties);

    @Test
    public void knowsApplicationName() throws Exception {
        properties.put("update.applicationName", "testname");
        assertThat(configuration.getApplicationName(), is("testname"));
    }

    @Test
    public void knowsFixedVersionStrategy() throws Exception {
        properties.put("update.strategy", "FixedVersion");
        assertThat(configuration.getStrategy(), is(UpdateStrategy.FixedVersion));
    }

    @Test
    public void knowsLatestVersionStrategy() throws Exception {
        properties.put("update.strategy", "LatestVersion");
        assertThat(configuration.getStrategy(), is(UpdateStrategy.LatestVersion));
    }

    @Test
    public void knowsLocationForFixedVersion() throws Exception {
        properties.put("update.FixedVersion.location", "./lib");
        assertThat(configuration.getLocationForFixedVersion(), is("./lib"));
    }

    @Test
    public void knowsHttpRepositories() throws Exception {
        properties.put("update.LatestVersion.repository.type", "HTTP");
        assertThat(configuration.repositoryTypeForLatestVersion(), is(RepositoryType.HTTP));
    }

    @Test
    public void knowsFileRepositories() throws Exception {
        properties.put("update.LatestVersion.repository.type", "File");
        assertThat(configuration.repositoryTypeForLatestVersion(), is(RepositoryType.File));
    }

    @Test
    public void knowsLocationOfRepositoryForLatestVersion() throws Exception {
        properties.put("update.LatestVersion.repository.location", "./aPath");
        assertThat(configuration.repositoryLocationForLatestVersion(), is("./aPath"));
    }
}