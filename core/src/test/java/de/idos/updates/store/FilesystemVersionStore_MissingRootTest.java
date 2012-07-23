package de.idos.updates.store;

import de.idos.updates.NumericVersion;
import de.idos.updates.VersionFinder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FilesystemVersionStore_MissingRootTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void createsRootFolderIfItDoesNotExist() throws Exception {
        File root = folder.newFolder();
        root.delete();
        FilesystemVersionStore store = new FilesystemVersionStore(root);
        assertThat(store.getLatestVersion(), is(VersionFinder.BASE_VERSION));
    }
}