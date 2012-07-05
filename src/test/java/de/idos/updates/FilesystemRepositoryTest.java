package de.idos.updates;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FilesystemRepositoryTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void fillRepository() throws Exception {
        File available_versions = folder.newFolder("available_versions");
        new File(available_versions, "4.2.1").createNewFile();
    }

    @Test
    public void reportsLatestVersionFromFile() throws Exception {
        FilesystemRepository repository = new FilesystemRepository(folder.getRoot());
        Version version = repository.getLatestVersion();
        assertThat(version.isEqualTo(new NumericVersion(4, 2, 1)), is(true));
    }
}
