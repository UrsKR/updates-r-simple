package de.idos.updates;

import de.idos.updates.store.InstallationUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class VersionedFileFactoryTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void doesNotListVersionsWhereAnUpdateIsInProgress() throws Exception {
        folder.newFolder("4.0.0");
        File v401 = folder.newFolder("4.0.1");
        InstallationUtil.createMarkerFile(v401);
        List<VersionedFile> versionedFiles = new VersionedFileFactory().createVersionedFilesFrom(folder.getRoot());
        assertThat(versionedFiles.size(), is(1));
    }
}
