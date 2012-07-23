package de.idos.updates.store;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FileDataInVersionTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Test
    public void equalsFileDataWithSameFile() throws Exception {
        FileDataInVersion dataInVersion = new FileDataInVersion(folder.getRoot());
        FileDataInVersion dataInVersion2 = new FileDataInVersion(folder.getRoot());
        assertThat(dataInVersion.equals(dataInVersion2), is(true));
    }

    @Test
    public void doesNotEqualDataWithDifferentFile() throws Exception {
        FileDataInVersion dataInVersion = new FileDataInVersion(folder.newFile());
        FileDataInVersion dataInVersion2 = new FileDataInVersion(folder.newFile());
        assertThat(dataInVersion.equals(dataInVersion2), is(false));
    }

    @Test
    public void addsToGivenImport() throws Exception {
        DataImport dataImport = mock(DataImport.class);
        when(dataImport.takeDataFromFactory(any(InputStreamFactory.class))).thenReturn(dataImport);
        File file = folder.newFile();
        File versionFolder = folder.newFolder();
        new FileDataInVersion(file, dataImport).storeIn(versionFolder);
        verify(dataImport).takeDataFromFactory(isA(FileStreamFactory.class));
        verify(dataImport).andStoreThemIn(versionFolder, file.getName());
    }
}