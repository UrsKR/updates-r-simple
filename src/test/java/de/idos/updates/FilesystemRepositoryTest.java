package de.idos.updates;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static de.idos.updates.NumericVersionMatchers.sameVersionAs;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FilesystemRepositoryTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private File available_versions;

    @Before
    public void fillRepository() throws Exception {
        available_versions = folder.newFolder("available_versions");
    }

    @Test
    public void reportsLatestVersionFromFile() throws Exception {
        addVersion("4.2.1");
        Version version = getLatestVersionFromRepository();
        assertThat(version, is(sameVersionAs(new NumericVersion(4, 2, 1))));
    }

    @Test
    public void reportsLatestVersionFromAllRegistered() throws Exception {
        addVersion("4.2.1");
        addVersion("4.2.2");
        Version version = getLatestVersionFromRepository();
        assertThat(version, is(sameVersionAs(new NumericVersion(4, 2, 2))));
    }

    private void addVersion(String versionNumber) throws IOException {
        new File(available_versions, versionNumber).createNewFile();
    }

    private Version getLatestVersionFromRepository() {
        FilesystemRepository repository = new FilesystemRepository(folder.getRoot());
        return repository.getLatestVersion();
    }
}