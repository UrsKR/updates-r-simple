package de.idos.updates.store;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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
}
