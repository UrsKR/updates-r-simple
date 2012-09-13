package de.idos.updates.store;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UnzipperTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Installation wrapped = mock(Installation.class);
    private Unzipper unzipper = new Unzipper(wrapped);

    @Test
    public void unzipsArchivesAndForwardsEntriesToWrapped() throws Exception {
        final File content = ZipFileMother.createContentFileForZip(folder.getRoot());
        File sourceFolder = folder.newFolder("source");
        File targetFolder = folder.newFolder("target");
        ZipFileMother.createZipFileInTemporaryFolder(sourceFolder, "iAmA.zip", content);
        unzipper.unzipAllArchivesInDirectory(sourceFolder, targetFolder);
        ArgumentCaptor<DataInVersion> captor = ArgumentCaptor.forClass(DataInVersion.class);
        verify(wrapped).addContent(captor.capture());
        DataInVersion installedData = captor.getValue();
        File checkoutFolder = folder.newFolder();
        installedData.storeIn(checkoutFolder);
        assertThatFilesAreSimilar(content, checkoutFolder.listFiles()[0]);
    }

    private void assertThatFilesAreSimilar(File content, File file) throws IOException {
        assertThat(FileUtils.contentEquals(content, file), is(true));
        assertThat(content.getName(), is(file.getName()));
    }
}