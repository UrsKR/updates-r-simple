package de.idos.updates.store;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ZipInstallationTest {
    @Rule
    public TemporaryFolder stagingFolder = new TemporaryFolder();

    private Installation wrapped = mock(Installation.class);
    private ProgressReport report = mock(ProgressReport.class);
    private ZipInstallation installation = new ZipInstallation(wrapped, report);
    private DataInVersion dataInVersion = mock(DataInVersion.class);

    @Test
    public void unzipsArchivesAndForwardsEntriesToWrapped() throws Exception {
        installFromZip();
        verify(wrapped).addContent(Matchers.isA(DataInVersion.class));
    }

    @Test
    public void deletesAllTemporaryFiles() throws Exception {
        installFromZip();
        assertThatNoTemporariesRemain();
    }

    private void assertThatNoTemporariesRemain() {
        String[] temporaryDirectories = stagingFolder.getRoot().getParentFile().list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("updates-r-us");
            }
        });
        assertThat(temporaryDirectories.length, is(0));
    }

    @Test
    public void reportsErrorsToReport() throws Exception {
        RuntimeException exception = new RuntimeException();
        Mockito.doThrow(exception).when(dataInVersion).storeIn(isA(File.class));
        installation.addContent(dataInVersion);
        verify(report).installationFailed(exception);
    }

    @Test
    public void deletesTemporaryFilesEvenWhenAnErrorOccurs() throws Exception {
        RuntimeException exception = new RuntimeException();
        Mockito.doThrow(exception).when(dataInVersion).storeIn(isA(File.class));
        installation.addContent(dataInVersion);
        assertThatNoTemporariesRemain();
    }

    private void installFromZip() throws IOException {
        final File content = ZipFileMother.createContentFileForZip(stagingFolder.getRoot());
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                File targetFolder = (File) invocation.getArguments()[0];
                ZipFileMother.createZipFileInTemporaryFolder(targetFolder, "iAmA.zip", content);
                return null;
            }
        }).when(dataInVersion).storeIn(Matchers.isA(File.class));
        installation.addContent(dataInVersion);
    }
}