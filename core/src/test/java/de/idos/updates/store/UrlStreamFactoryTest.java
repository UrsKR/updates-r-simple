package de.idos.updates.store;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UrlStreamFactoryTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void reportsContentLengthAsSize() throws Exception {
        File file = folder.newFile();
        FileUtils.writeByteArrayToFile(file, new byte[3000]);
        URL url = file.toURI().toURL();
        long expectedSize = new UrlStreamFactory(url).getExpectedSize();
        assertThat(expectedSize, is(3000L));
    }
}
