package de.idos.updates;

import de.idos.updates.store.FileDataInVersion;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.InOrder;

import java.io.File;
import java.io.IOException;

import static de.idos.updates.NumericVersionMatchers.sameVersionAs;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class FilesystemRepositoryTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private File available_versions;
    private VersionStore store = mock(VersionStore.class);

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

    @Test
    public void transfersVersionToStore() throws Exception {
        addVersion("4.2.1");
        File content = addContentToVersion("4.2.1", "content");
        NumericVersion version = new NumericVersion(4, 2, 1);
        new FilesystemRepository(folder.getRoot()).transferVersionTo(version, store);
        InOrder inOrder = inOrder(store);
        inOrder.verify(store).addVersion(version);
        inOrder.verify(store).addContent(version, new FileDataInVersion(content));
    }

    @Test
    public void createsRepositoryFolderIfItDoesNotExist() throws Exception {
        available_versions.delete();
        new FilesystemRepository(folder.getRoot());
        assertThat(available_versions.exists(), is(true));
    }

    private File addContentToVersion(String versionNumber, String fileName) throws IOException {
        File versionFolder = new File(available_versions, versionNumber);
        File file = new File(versionFolder, fileName);
        file.createNewFile();
        return file;
    }

    private void addVersion(String versionNumber) throws IOException {
        new File(available_versions, versionNumber).mkdir();
    }

    private Version getLatestVersionFromRepository() {
        FilesystemRepository repository = new FilesystemRepository(folder.getRoot());
        return repository.getLatestVersion();
    }
}