package de.idos.updates.store;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ZipInstallation_NonZipTest {
    @Rule
    public TemporaryFolder stagingFolder = new TemporaryFolder();

    private Installation wrapped = mock(Installation.class);
    private ZipInstallation installation = new ZipInstallation(wrapped);
    private DataInVersion dataInVersion = mock(DataInVersion.class);

    @Test
    public void installsDataToTemporaryDirectory() throws Exception {
        installation.addContent(dataInVersion);
        ArgumentCaptor<File> captor = ArgumentCaptor.forClass(File.class);
        verify(dataInVersion).storeIn(captor.capture());
        File tempFolder = captor.getValue();
        File randomTempFile = File.createTempFile("testfile", "xxx");
        randomTempFile.deleteOnExit();
        File systemTempFolder = randomTempFile.getParentFile();
        assertThat(tempFolder.getParentFile(), is(systemTempFolder));
    }

    @Test
    public void forwardsNonZipDataDirectlyToWrapped() throws Exception {
        final File[] createdFile = new File[1];
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                File targetFolder = (File) invocation.getArguments()[0];
                File notAZip = createFileInTemporaryFolder(targetFolder, "iAmNotAZip.jar");
                createdFile[0] = notAZip;
                return null;
            }
        }).when(dataInVersion).storeIn(Matchers.isA(File.class));
        installation.addContent(dataInVersion);
        verify(wrapped).addContent(new FileDataInVersion(createdFile[0]));
    }

    @Test
    public void deletesFilesOnceForwarded() throws Exception {
        final File[] createdFile = new File[1];
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                File targetFolder = (File) invocation.getArguments()[0];
                File notAZip = createFileInTemporaryFolder(targetFolder, "iAmNotAZip.jar");
                createdFile[0] = notAZip;
                return null;
            }
        }).when(dataInVersion).storeIn(Matchers.isA(File.class));
        installation.addContent(dataInVersion);
        assertThat(createdFile[0].exists(), is(false));
    }

    private File createFileInTemporaryFolder(File targetFolder, String name) throws IOException {
        File notAZip = new File(targetFolder, name);
        notAZip.createNewFile();
        notAZip.deleteOnExit();
        return notAZip;
    }
}
